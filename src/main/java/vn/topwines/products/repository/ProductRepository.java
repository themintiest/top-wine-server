package vn.topwines.products.repository;

import io.quarkus.panache.common.Parameters;
import vn.topwines.core.repository.BaseRepository;
import vn.topwines.products.entity.Product;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class ProductRepository extends BaseRepository<Product, Long> {

    public Optional<Product> findByCodeAndIsDeleted(String code, Boolean isDeleted) {
        Parameters parameters = Parameters.with("code", code).and("isDeleted", isDeleted);
        return find("code = :code and isDeleted = :isDeleted", parameters).singleResultOptional();
    }

    public Boolean existByCodeAndIgnoreId(String code, Long id) {
        Parameters parameters = Parameters.with("code", code).and("id", id);
        return find("code = :code and id != :id", parameters).count() > 0;
    }

    public Optional<Product> findAvailableById(Long id, Integer quantity) {
        Parameters parameters = Parameters.with("id", id).and("quantity", quantity);
        return find("id = :id and isDeleted = false and quantity >= :quantity", parameters).singleResultOptional();
    }

    public List<Product> findByIdIn(Set<Long> ids) {
        Parameters parameters = Parameters.with("ids", ids);
        return find("id in :ids", parameters).list();
    }
}
