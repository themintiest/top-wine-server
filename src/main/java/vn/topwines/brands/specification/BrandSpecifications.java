package vn.topwines.brands.specification;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import vn.topwines.brands.entity.Brand;
import vn.topwines.core.repository.specification.Specification;

public class BrandSpecifications {

    public static BrandSpecificationsBuilder builder() {
        return new BrandSpecificationsBuilder();
    }

    @Getter
    @Setter
    public static class BrandSpecificationsBuilder {
        private String name;
        private String code;
        private Boolean isDeleted;

        public BrandSpecificationsBuilder withNameLike(String name) {
            this.name = name;
            return this;
        }

        public BrandSpecificationsBuilder withCode(String code) {
            this.code = code;
            return this;
        }

        public BrandSpecificationsBuilder isDeleted(Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public Specification<Brand> build() {
            Specification<Brand> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
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

