package vn.topwines.booking.constant;

import lombok.Getter;

@Getter
public enum PaymentMethodCode {
    CASH_ON_DELIVERY("Thanh toán khi nhận hàng");

    private final String name;

    PaymentMethodCode(String name) {
        this.name = name;
    }
}
