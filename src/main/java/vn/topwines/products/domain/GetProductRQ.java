package vn.topwines.products.domain;

import lombok.Getter;
import lombok.Setter;
import vn.topwines.core.query.PagingRQ;

import java.math.BigDecimal;

@Getter
@Setter
public class GetProductRQ extends PagingRQ {
    private String name;
    private String code;
    private String productCode;
    private Long categoryId;
    private Long regionId;
    private Long grapeId;
    private Long nationId;
    private Long brandId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Boolean isDeleted;
}
