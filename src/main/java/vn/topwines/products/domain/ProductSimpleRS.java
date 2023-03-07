package vn.topwines.products.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductSimpleRS {
    private Long id;
    private String name;
    private String code;
    private String productCode;
    private String image;
    private BigDecimal price;
    private BigDecimal discount;
}
