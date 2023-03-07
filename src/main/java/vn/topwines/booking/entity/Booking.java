package vn.topwines.booking.entity;

import lombok.Getter;
import lombok.Setter;
import vn.topwines.booking.constant.BookingStatus;
import vn.topwines.booking.constant.PaymentMethodCode;
import vn.topwines.booking.constant.PaymentStatus;
import vn.topwines.core.entity.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "bookings")
public class Booking extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "address", columnDefinition = "text")
    private String address;

    @Column(name = "booking_amount")
    private BigDecimal bookingAmount;

    @Column(name = "shipping_fee")
    private BigDecimal shippingFee = new BigDecimal(0);

    @Column(name = "other_fee")
    private BigDecimal otherFee = new BigDecimal(0);

    @Column(name = "tax")
    private BigDecimal tax = new BigDecimal(0);

    @Column(name = "discount_amount")
    private BigDecimal discountAmount = new BigDecimal(0);

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "promotion_code")
    private String promotionCode;

    @Column(name = "customer_note")
    private String customerNote;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method_code")
    private PaymentMethodCode paymentMethodCode = PaymentMethodCode.CASH_ON_DELIVERY;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status")
    private BookingStatus bookingStatus = BookingStatus.PENDING;

    @Column(name = "is_deleted")
    private Boolean isDeleted = Boolean.FALSE;

    @Column(name = "payment_time")
    private Instant paymentTime;

    @Column(name = "completed_time")
    private Instant completedTime;

    @Column(name = "cancelled_time")
    private Instant cancelledTime;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingProduct> bookingProducts = new ArrayList<>();
}
