package vn.topwines.categories.specifications;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import vn.topwines.categories.entity.Category;
import vn.topwines.core.repository.specification.Specification;

public class CategorySpecifications {

    public static CategorySpecificationsBuilder builder() {
        return new CategorySpecificationsBuilder();
    }

    @Getter
    @Setter
    public static class CategorySpecificationsBuilder {
        private String name;
        private String code;
        private Boolean isDeleted;
        private Boolean withOutParent;

        public CategorySpecificationsBuilder withNameLike(String name) {
            this.name = name;
            return this;
        }

        public CategorySpecificationsBuilder withCode(String code) {
            this.code = code;
            return this;
        }

        public CategorySpecificationsBuilder isDeleted(Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public CategorySpecificationsBuilder withOutParent(Boolean withOutParent) {
            this.withOutParent = withOutParent;
            return this;
        }

        public Specification<Category> build() {
            Specification<Category> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
            if (StringUtils.isNotBlank(name)) {
                specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), String.format("%%%s%%", name.toLowerCase())));
            }
            if (StringUtils.isNotBlank(code)) {
                specification = specification.and((root, query, cb) -> cb.like(root.get("code"), code));
            }
            if (isDeleted != null) {
                specification = specification.and((root, query, cb) -> cb.equal(root.get("isDeleted"), isDeleted));
            }
            if (withOutParent != null && withOutParent) {
                specification = specification.and((root, query, cb) -> cb.isNull(root.get("parent")));
            }
            return specification;
        }
    }
}
