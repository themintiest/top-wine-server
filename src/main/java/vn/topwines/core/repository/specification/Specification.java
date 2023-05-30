package vn.topwines.core.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.io.Serializable;

import static vn.topwines.core.repository.specification.SpecificationComposition.composed;

public interface Specification<T> extends Serializable {
    long serialVersionUID = 1L;

    static <T> Specification<T> not(Specification<T> spec) {

        return spec == null //
                ? (root, query, builder) -> null//
                : (root, query, builder) -> builder.not(spec.toPredicate(root, query, builder));
    }

    static <T> Specification<T> where(Specification<T> spec) {
        return spec == null ? (root, query, builder) -> null : spec;
    }

    default Specification<T> and(Specification<T> other) {
        return composed(this, other, CriteriaBuilder::and);
    }


    default Specification<T> or(Specification<T> other) {
        return composed(this, other, CriteriaBuilder::or);
    }

    Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder);
}
