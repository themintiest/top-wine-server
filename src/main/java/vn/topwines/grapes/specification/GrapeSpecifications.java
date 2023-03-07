package vn.topwines.grapes.specification;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import vn.topwines.core.repository.specification.Specification;
import vn.topwines.grapes.entity.Grape;

public class GrapeSpecifications {

    public static GrapeSpecificationsBuilder builder() {
        return new GrapeSpecificationsBuilder();
    }

    @Getter
    @Setter
    public static class GrapeSpecificationsBuilder {
        private String name;
        private String code;
        private Boolean isDeleted;

        public GrapeSpecificationsBuilder withNameLike(String name) {
            this.name = name;
            return this;
        }

        public GrapeSpecificationsBuilder withCode(String code) {
            this.code = code;
            return this;
        }

        public GrapeSpecificationsBuilder isDeleted(Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public Specification<Grape> build() {
            Specification<Grape> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
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

