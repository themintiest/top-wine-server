package vn.topwines.products.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UpdateProductRQ {
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
    private Integer createdYear;
    private String description;
    private String seoTitle;
    private String seoDescription;
    private String robotTag;
    private String canonical;
    private String keywords;
    private Boolean isDeleted;
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
        private Long id;
        private String image;
    }
}
