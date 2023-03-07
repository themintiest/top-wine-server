package vn.topwines.product_type.specification;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import vn.topwines.core.repository.specification.Specification;
import vn.topwines.product_type.entity.ProductType;

public class ProductTypeSpecifications {

    public static ProductTypeSpecificationsBuilder builder() {
        return new ProductTypeSpecificationsBuilder();
    }

    @Getter
    @Setter
    public static class ProductTypeSpecificationsBuilder {
        private String name;
        private String code;
        private Boolean isDeleted;

        public ProductTypeSpecificationsBuilder withNameLike(String name) {
            this.name = name;
            return this;
        }

        public ProductTypeSpecificationsBuilder withCode(String code) {
            this.code = code;
            return this;
        }

        public ProductTypeSpecificationsBuilder isDeleted(Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public Specification<ProductType> build() {
            Specification<ProductType> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
            if (StringUtils.isNotBlank(name)) {
                specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), String.format("%%%s%%", name.toLowerCase())));
            }
            if (StringUtils.isNotBlank(code)) {
                specification = specification.and((root, query, cb) -> cb.like(root.get("code"), code));
            }
            if (isDeleted != null) {
                specification = specification.and((root, query, cb) -> cb.equal(root.get("isDeleted"), isDeleted));
            }
            return specification;
        }
    }
}

