package vn.topwines.nations.specification;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import vn.topwines.core.repository.specification.Specification;
import vn.topwines.nations.entity.Nation;

public class NationSpecifications {

    public static NationSpecificationsBuilder builder() {
        return new NationSpecificationsBuilder();
    }

    @Getter
    @Setter
    public static class NationSpecificationsBuilder {
        private String name;
        private String code;
        private Boolean isDeleted;

        public NationSpecificationsBuilder withNameLike(String name) {
            this.name = name;
            return this;
        }

        public NationSpecificationsBuilder withCode(String code) {
            this.code = code;
            return this;
        }

        public NationSpecificationsBuilder isDeleted(Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public Specification<Nation> build() {
            Specification<Nation> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
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

