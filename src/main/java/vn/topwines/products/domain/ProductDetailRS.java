package vn.topwines.products.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import vn.topwines.brands.domain.BrandSimpleRS;
import vn.topwines.categories.domain.CategorySimple;
import vn.topwines.core.utils.InstantEpochMillisSerializer;
import vn.topwines.grapes.domain.GrapeSimpleRS;
import vn.topwines.nations.domain.NationSimpleRS;
import vn.topwines.product_type.domain.ProductTypeSimpleRS;
import vn.topwines.regions.domain.RegionSimpleRS;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class ProductDetailRS {
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
    private Integer createdYear;
    private String description;
    private String seoTitle;
    private String seoDescription;
    private String robotTag;
    private String canonical;
    private String keywords;
    private Boolean isDeleted;

    @JsonSerialize(using = InstantEpochMillisSerializer.class)
    private Instant createdTime;

    @JsonSerialize(using = InstantEpochMillisSerializer.class)
    private Instant updatedTime;

    private RegionSimpleRS region;
    private GrapeSimpleRS grape;
    private BrandSimpleRS brand;
    private NationSimpleRS nation;
    private ProductTypeSimpleRS productType;
    private List<CategorySimple> categories;
    private List<ProductImageRS> productImages;
}
