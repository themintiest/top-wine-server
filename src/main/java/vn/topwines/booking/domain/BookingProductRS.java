package vn.topwines.booking.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BookingProductRS {
    private Long id;
    private Integer quantity;
    private Long productId;
    private String productCode;
    private BigDecimal productDiscount;
    private String productName;
    private String productImage;
    private String productDescription;
    private BigDecimal productPrice;
    private BigDecimal totalPrice;
    private Integer productCreatedYear;
}
