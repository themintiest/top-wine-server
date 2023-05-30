package vn.topwines.core.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.io.Serializable;

public interface SpecificationComposition extends Serializable {
    interface Combiner extends Serializable {
        Predicate combine(CriteriaBuilder builder, Predicate lhs, Predicate rhs);
    }

    static <T> Specification<T> composed(Specification<T> lhs, Specification<T> rhs, Combiner combiner) {
        return (root, query, builder) -> {
            Predicate otherPredicate = toPredicate(lhs, root, query, builder);
            Predicate thisPredicate = toPredicate(rhs, root, query, builder);

            if (thisPredicate == null) {
                return otherPredicate;
            }

            return otherPredicate == null ? thisPredicate : combiner.combine(builder, thisPredicate, otherPredicate);
        };
    }

    private static <T> Predicate toPredicate(Specification<T> specification, Root<T> root, CriteriaQuery<?> query,
                                             CriteriaBuilder builder) {
        return specification == null ? null : specification.toPredicate(root, query, builder);
    }
}
