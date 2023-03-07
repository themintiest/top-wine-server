package vn.topwines.products.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import vn.topwines.core.utils.InstantEpochMillisSerializer;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class ProductRS {
    private Long id;
    private String name;
    private String code;
    private String productCode;
    private String image;
    private BigDecimal price;
    private BigDecimal discount;
    private Integer quantity;
    private Integer sold;
    private Integer capacity;
    private Double concentration;
    private String seoTitle;
    private String seoDescription;
    private String robotTag;
    private String keywords;
    private String canonical;

    @JsonSerialize(using = InstantEpochMillisSerializer.class)
    private Instant createdTime;

    @JsonSerialize(using = InstantEpochMillisSerializer.class)
    private Instant updatedTime;
}
