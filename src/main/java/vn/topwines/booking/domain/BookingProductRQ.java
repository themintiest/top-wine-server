package vn.topwines.booking.domain;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class BookingProductRQ {
    @NotNull
    private Long productId;

    @NotNull
    private Integer quantity;
}
