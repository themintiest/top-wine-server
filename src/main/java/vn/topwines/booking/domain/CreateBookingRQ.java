package vn.topwines.booking.domain;

import lombok.Getter;
import lombok.Setter;
import vn.topwines.booking.constant.PaymentMethodCode;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class CreateBookingRQ {
    @NotBlank
    private String customerName;
    private String customerEmail;
    @NotBlank
    private String customerPhone;
    @NotBlank
    private String address;
    private String promotionCode;
    private String customerNote;
    private PaymentMethodCode paymentMethodCode = PaymentMethodCode.CASH_ON_DELIVERY;

    @Valid
    private List<BookingProductRQ> bookingProducts;
}
