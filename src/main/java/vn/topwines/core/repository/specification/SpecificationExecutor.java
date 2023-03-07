package vn.topwines.core.repository.specification;

import io.quarkus.panache.common.Sort;
import vn.topwines.core.query.PageRequest;
import vn.topwines.core.query.Pageable;

import java.util.List;

public interface SpecificationExecutor<T> {

    List<T> findAll(Specification<T> spec);

    Pageable<T> findAll(Specification<T> spec, PageRequest pageRequest);

    List<T> findAll(Specification<T> spec, Sort sort);

    long count(Specification<T> spec);
}
