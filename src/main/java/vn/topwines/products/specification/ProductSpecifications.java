package vn.topwines.products.specification;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import vn.topwines.core.repository.specification.Specification;
import vn.topwines.products.entity.Product;
import vn.topwines.products.entity.ProductCategory;

import jakarta.persistence.criteria.Join;
import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductSpecifications {
    public static ProductSpecificationsBuilder builder() {
        return new ProductSpecificationsBuilder();
    }

    @Getter
    @Setter
    public static class ProductSpecificationsBuilder {
        private String name;
        private String code;
        private String productCode;
        private Boolean isDeleted;
        private Long categoryId;
        private Long regionId;
        private Long grapeId;
        private Long brandId;
        private Long nationId;
        private BigDecimal minPrice;
        private BigDecimal maxPrice;

        public ProductSpecificationsBuilder withNameLike(String name) {
            this.name = name;
            return this;
        }

        public ProductSpecificationsBuilder withCode(String code) {
            this.code = code;
            return this;
        }

        public ProductSpecificationsBuilder withProductCode(String productCode) {
            this.productCode = productCode;
            return this;
        }

        public ProductSpecificationsBuilder isDeleted(Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public ProductSpecificationsBuilder withCategoryId(Long categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public ProductSpecificationsBuilder withRegionId(Long regionId) {
            this.regionId = regionId;
            return this;
        }

        public ProductSpecificationsBuilder withGrapeId(Long grapeId) {
            this.grapeId = grapeId;
            return this;
        }

        public ProductSpecificationsBuilder withBrandId(Long brandId) {
            this.brandId = brandId;
            return this;
        }

        public ProductSpecificationsBuilder withNationId(Long nationId) {
            this.nationId = nationId;
            return this;
        }

        public ProductSpecificationsBuilder withMinPrice(BigDecimal minPrice) {
            this.minPrice = minPrice;
            return this;
        }

        public ProductSpecificationsBuilder withMaxPrice(BigDecimal maxPrice) {
            this.maxPrice = maxPrice;
            return this;
        }

        public Specification<Product> build() {
            Specification<Product> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
            if (StringUtils.isNotBlank(name)) {
                specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), String.format("%%%s%%", name.toLowerCase())));
            }
            if (StringUtils.isNotBlank(code)) {
                specification = specification.and((root, query, cb) -> cb.like(root.get("code"), code));
            }
            if (StringUtils.isNotBlank(productCode)) {
                specification = specification.and((root, query, cb) -> cb.like(root.get("productCode"), productCode));
            }
            if (isDeleted != null) {
                specification = specification.and((root, query, cb) -> cb.equal(root.get("isDeleted"), isDeleted));
            }
            if (categoryId != null) {
                specification = specification.and((root, query, criteriaBuilder) -> {
                    query.distinct(true);
                    Join<Product, ProductCategory> join = root.join("productCategories");
                    return criteriaBuilder.equal(join.get("category").get("id"), categoryId);
                });
            }
            if (regionId != null) {
                specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("region").get("id"), regionId));
            }
            if (grapeId != null) {
                specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("grape").get("id"), grapeId));
            }
            if (brandId != null) {
                specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("brand").get("id"), brandId));
            }
            if (nationId != null) {
                specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("nation").get("id"), nationId));
            }
            if (minPrice != null) {
                specification = specification.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                specification = specification.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            return specification;
        }
    }
}
