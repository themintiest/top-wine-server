package vn.topwines.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.topwines.booking.domain.BookingProductRS;
import vn.topwines.booking.entity.BookingProduct;
import vn.topwines.products.domain.UpdateProductQuantityMessage;
import vn.topwines.products.entity.Product;

@Mapper(
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
)
public interface BookingProductMapper {
    BookingProductMapper INSTANCE = Mappers.getMapper(BookingProductMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quantity", ignore = true)
    @Mapping(target = "productName", source = "name")
    @Mapping(target = "productCode", source = "code")
    @Mapping(target = "productImage", source = "image")
    @Mapping(target = "productDiscount", source = "discount")
    @Mapping(target = "productDescription", source = "description")
    @Mapping(target = "productPrice", source = "price")
    @Mapping(target = "productCreatedYear", source = "createdYear")
    BookingProduct mapFromProductToBookingProduct(Product product);

    @Mapping(target = "productId", source = "product.id")
    BookingProductRS mapBookingProductToBookingProductRS(BookingProduct bookingProduct);

    @Mapping(target = "productId", source = "product.id")
    UpdateProductQuantityMessage.Item mapBookingProductToUpdateProductQuantityMessageItem(BookingProduct bookingProduct);
}
