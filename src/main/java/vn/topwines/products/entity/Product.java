package vn.topwines.products.entity;

import lombok.Getter;
import lombok.Setter;
import vn.topwines.brands.entity.Brand;
import vn.topwines.core.entity.BaseEntity;
import vn.topwines.grapes.entity.Grape;
import vn.topwines.nations.entity.Nation;
import vn.topwines.product_type.entity.ProductType;
import vn.topwines.regions.entity.Region;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
    private String keywords;
    private BigDecimal price = new BigDecimal(0);
    private BigDecimal discount = new BigDecimal(0);
    private Integer quantity = 0;
    private Integer sold = 0;
    private Integer capacity = 0;
    private Double concentration = 0D;

    @Column(name = "created_year")
    private Integer createdYear;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "image", columnDefinition = "text")
    private String image;

    @Column(name = "seo_title")
    private String seoTitle;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "seo_description", columnDefinition = "text")
    private String seoDescription;

    @Column(name = "robot_tag")
    private String robotTag;

    @Column(name = "canonical", columnDefinition = "text")
    private String canonical;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", referencedColumnName = "id")
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grape_id", referencedColumnName = "id")
    private Grape grape;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nation_id", referencedColumnName = "id")
    private Nation nation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_type_id", referencedColumnName = "id")
    private ProductType productType;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> productImages = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductCategory> productCategories = new ArrayList<>();
}
