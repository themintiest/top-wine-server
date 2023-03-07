package vn.topwines.booking.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import vn.topwines.booking.constant.BookingStatus;
import vn.topwines.booking.constant.PaymentMethodCode;
import vn.topwines.booking.constant.PaymentStatus;
import vn.topwines.core.utils.InstantEpochMillisSerializer;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class BookingRS {
    private Long id;
    private String code;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String address;
    private BigDecimal bookingAmount;
    private BigDecimal shippingFee;
    private BigDecimal otherFee;
    private BigDecimal tax;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String promotionCode;
    private String customerNote;
    private PaymentMethodCode paymentMethodCode;
    private PaymentStatus paymentStatus;
    private BookingStatus bookingStatus;
    private Boolean isDeleted;
    private Instant paymentTime;

    @JsonSerialize(using = InstantEpochMillisSerializer.class)
    private Instant completedTime;

    @JsonSerialize(using = InstantEpochMillisSerializer.class)
    private Instant cancelledTime;

    @JsonSerialize(using = InstantEpochMillisSerializer.class)
    private Instant createdTime;

    @JsonSerialize(using = InstantEpochMillisSerializer.class)
    private Instant updatedTime;
}
