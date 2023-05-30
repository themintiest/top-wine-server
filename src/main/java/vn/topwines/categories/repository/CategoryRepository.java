package vn.topwines.categories.repository;

import io.quarkus.panache.common.Parameters;
import vn.topwines.categories.entity.Category;
import vn.topwines.core.repository.BaseRepository;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class CategoryRepository extends BaseRepository<Category, Long> {

    public Optional<Category> findActiveByIdOptional(Long id) {
        Parameters parameters = Parameters.with("id", id);
        return find("id = :id and isDeleted = false", parameters).firstResultOptional();
    }

    public Optional<Category> findByCodeAndIsDeletedOptional(String code, Boolean isDeleted) {
        Parameters parameters = Parameters.with("code", code).and("isDeleted", isDeleted);
        return find("code = :code and isDeleted = :isDeleted", parameters).firstResultOptional();
    }

    public List<Category> findByParentId(Long parentId) {
        return find("parent.id", parentId).list();
    }

    public List<Category> findActiveByIdIn(Set<Long> ids) {
        Parameters parameters = Parameters.with("ids", ids);
        return find("id in :ids and isDeleted = false", parameters).list();
    }

    public Boolean existsByCode(String code) {
        return exists("code", code);
    }

    public Boolean existsByCodeAndIgnoreId(String code, Long id) {
        Parameters parameters = Parameters.with("code", code).and("id", id);
        return find("code = :code and id != :id", parameters).count() > 0;
    }
}
