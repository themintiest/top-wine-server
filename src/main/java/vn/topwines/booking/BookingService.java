package vn.topwines.booking;

import io.vertx.core.eventbus.EventBus;
import lombok.RequiredArgsConstructor;
import vn.topwines.booking.constant.BookingStatus;
import vn.topwines.booking.constant.PaymentStatus;
import vn.topwines.booking.domain.BookingDetailRS;
import vn.topwines.booking.domain.BookingProductRQ;
import vn.topwines.booking.domain.BookingRS;
import vn.topwines.booking.domain.CreateBookingRQ;
import vn.topwines.booking.domain.GetBookingRQ;
import vn.topwines.booking.entity.Booking;
import vn.topwines.booking.entity.BookingProduct;
import vn.topwines.booking.mapper.BookingMapper;
import vn.topwines.booking.mapper.BookingProductMapper;
import vn.topwines.booking.repository.BookingRepository;
import vn.topwines.booking.specification.BookingSpecifications;
import vn.topwines.common.constant.EventCode;
import vn.topwines.core.query.PageRequest;
import vn.topwines.core.query.Pageable;
import vn.topwines.core.repository.specification.Specification;
import vn.topwines.core.utils.MaskingRandomUtils;
import vn.topwines.core.utils.PageUtils;
import vn.topwines.exception.BadRequestException;
import vn.topwines.products.ProductService;
import vn.topwines.products.constant.UpdateQuantityType;
import vn.topwines.products.domain.UpdateProductQuantityMessage;
import vn.topwines.products.entity.Product;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
public class BookingService {
    private final EventBus eventBus;
    private final ProductService productService;
    private final BookingRepository bookingRepository;

    @Transactional
    public BookingDetailRS createBooking(CreateBookingRQ createBookingRQ) {
        Booking booking = BookingMapper.INSTANCE.mapCreateBookingRQ(createBookingRQ);
        List<BookingProduct> bookingProductList = new ArrayList<>(createBookingRQ.getBookingProducts().size());
        for (BookingProductRQ item : createBookingRQ.getBookingProducts()) {
            Product product = productService.getAvailableById(item.getProductId(), item.getQuantity());
            BookingProduct bookingProduct = BookingProductMapper.INSTANCE.mapFromProductToBookingProduct(product);
            bookingProduct.setBooking(booking);
            bookingProduct.setProduct(product);
            bookingProduct.setQuantity(item.getQuantity());
            bookingProduct.setTotalPrice(product.getPrice().multiply(new BigDecimal(item.getQuantity())));
            bookingProductList.add(bookingProduct);
        }
        booking.setBookingAmount(calculateBookingAmount(bookingProductList));
        BigDecimal totalAmount = booking.getBookingAmount()
                .add(booking.getShippingFee())
                .add(booking.getOtherFee())
                .add(booking.getTax());
        booking.setTotalAmount(totalAmount);
        booking.setBookingProducts(bookingProductList);
        Booking result = bookingRepository.save(booking, item -> {
            item.setCode(MaskingRandomUtils.generateCode(item.getId()));
        });
        List<UpdateProductQuantityMessage.Item> items = booking.getBookingProducts().stream()
                .map(BookingProductMapper.INSTANCE::mapBookingProductToUpdateProductQuantityMessageItem)
                .collect(Collectors.toList());
        UpdateProductQuantityMessage message = new UpdateProductQuantityMessage(UpdateQuantityType.DECREASE, items);
        eventBus.publish(EventCode.UPDATE_PRODUCT_QUANTITY, message);
        return BookingMapper.INSTANCE.mapBookingToBookingDetailRS(result);
    }

    @Transactional
    public BookingDetailRS updateBookingStatus(Long bookingId, BookingStatus status) {
        Booking booking = getById(bookingId);
        if (BookingStatus.PENDING.equals(status)) {
            return BookingMapper.INSTANCE.mapBookingToBookingDetailRS(booking);
        }
        if (BookingStatus.COMPLETED.equals(booking.getBookingStatus()) || BookingStatus.CANCELLED.equals(booking.getBookingStatus())) {
            throw new BadRequestException(String.format("Không thể chuyển trạng thái đơn hàng từ %s thành %s", booking.getBookingStatus(), status));
        }
        booking.setBookingStatus(status);
        if (BookingStatus.PAID.equals(status) || BookingStatus.COMPLETED.equals(status)) {
            Instant now = Instant.now();
            booking.setPaymentStatus(PaymentStatus.SUCCESS);
            booking.setPaymentTime(now);
            booking.setCompletedTime(now);
        } else if (BookingStatus.CANCELLED.equals(status)) {
            List<UpdateProductQuantityMessage.Item> items = booking.getBookingProducts().stream()
                    .map(BookingProductMapper.INSTANCE::mapBookingProductToUpdateProductQuantityMessageItem)
                    .collect(Collectors.toList());
            UpdateProductQuantityMessage message = new UpdateProductQuantityMessage(UpdateQuantityType.INCREASE, items);
            eventBus.publish(EventCode.UPDATE_PRODUCT_QUANTITY, message);
            booking.setPaymentStatus(PaymentStatus.FAILED);
            booking.setCancelledTime(Instant.now());
        }
        return BookingMapper.INSTANCE.mapBookingToBookingDetailRS(bookingRepository.save(booking));
    }

    public Pageable<BookingRS> getBookings(GetBookingRQ getBookingRQ) {
        PageRequest pageRequest = PageUtils.createPageRequest(getBookingRQ);
        Specification<Booking> specification = BookingSpecifications.builder()
                .withCode(getBookingRQ.getCode())
                .withCustomerName(getBookingRQ.getCustomerName())
                .withCustomerEmail(getBookingRQ.getCustomerEmail())
                .withCustomerPhone(getBookingRQ.getCustomerPhone())
                .withBookingStatus(getBookingRQ.getBookingStatus())
                .build();
        Pageable<Booking> bookingPageable = bookingRepository.findAll(specification, pageRequest);
        return PageUtils.createPageResponse(bookingPageable, BookingMapper.INSTANCE::mapBookingToBookingRS);
    }

    public BookingDetailRS getDetailByCode(String code) {
        return BookingMapper.INSTANCE.mapBookingToBookingDetailRS(getByCode(code));
    }

    public BookingDetailRS getDetailById(Long id) {
        return BookingMapper.INSTANCE.mapBookingToBookingDetailRS(getById(id));
    }

    private Booking getById(Long id) {
        return bookingRepository.findByIdOptional(id)
                .orElseThrow(() -> new BadRequestException("Đơn hàng không tồn tại"));
    }

    private Booking getByCode(String code) {
        return bookingRepository.findByCode(code)
                .orElseThrow(() -> new BadRequestException("Đơn hàng không tồn tại"));
    }

    private BigDecimal calculateBookingAmount(List<BookingProduct> bookingProducts) {
        BigDecimal amount = new BigDecimal(0);
        for (BookingProduct bookingProduct : bookingProducts) {
            amount = amount.add(bookingProduct.getTotalPrice());
        }
        return amount;
    }
}
