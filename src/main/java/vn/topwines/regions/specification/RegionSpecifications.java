package vn.topwines.regions.specification;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import vn.topwines.core.repository.specification.Specification;
import vn.topwines.regions.entity.Region;

public class RegionSpecifications {

    public static RegionSpecifications.RegionSpecificationsBuilder builder() {
        return new RegionSpecificationsBuilder();
    }

    @Getter
    @Setter
    public static class RegionSpecificationsBuilder {
        private String name;
        private String code;
        private Boolean isDeleted;
        private Boolean withOutParent;

        public RegionSpecificationsBuilder withNameLike(String name) {
            this.name = name;
            return this;
        }

        public RegionSpecificationsBuilder withCode(String code) {
            this.code = code;
            return this;
        }

        public RegionSpecificationsBuilder isDeleted(Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public Specification<Region> build() {
            Specification<Region> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
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

