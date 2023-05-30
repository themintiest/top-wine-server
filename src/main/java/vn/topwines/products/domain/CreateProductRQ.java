package vn.topwines.products.domain;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class CreateProductRQ {
    @NotBlank
    private String name;
    private String code;

    private String productCode;

    @NotBlank
    private String image;
    private BigDecimal price = new BigDecimal(0);
    private BigDecimal discount = new BigDecimal(0);
    private Integer quantity = 0;
    private Integer sold = 0;
    private Integer capacity = 0;
    private Double concentration = 0D;
    private Integer createdYear;
    private String description;
    private String seoTitle;
    private String seoDescription;
    private String robotTag;
    private String canonical;
    private String keywords;
    private Set<Long> categoryIds;
    private List<ProductImage> images;

    private Long regionId;
    private Long grapeId;
    private Long nationId;
    private Long brandId;
    private Long productTypeId;

    @Getter
    @Setter
    public static class ProductImage {
        private String image;
    }
}
