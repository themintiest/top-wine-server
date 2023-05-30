package vn.topwines.categories;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import vn.topwines.categories.domain.CategoryDetail;
import vn.topwines.categories.domain.CategoryRQ;
import vn.topwines.categories.domain.GetCategoryRQ;
import vn.topwines.categories.entity.Category;
import vn.topwines.categories.mapper.CategoryMapper;
import vn.topwines.categories.repository.CategoryRepository;
import vn.topwines.categories.specifications.CategorySpecifications;
import vn.topwines.core.query.PageRequest;
import vn.topwines.core.query.Pageable;
import vn.topwines.core.repository.specification.Specification;
import vn.topwines.core.utils.PageUtils;
import vn.topwines.exception.BadRequestException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;

@ApplicationScoped
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryDetail create(CategoryRQ categoryRQ) {
        String code = StringUtils.isNotBlank(categoryRQ.getCode())
                ? categoryRQ.getCode()
                : generateCategoryCode(categoryRQ.getName());
        if (categoryRepository.existsByCode(code)) {
            throw new BadRequestException("Mã danh mục đã tồn tại");
        }
        categoryRQ.setCode(code);
        Category category = CategoryMapper.INSTANCE.mapFromCategoryRQToCategory(categoryRQ);
        if (categoryRQ.getParentId() != null) {
            Category parent = getById(categoryRQ.getParentId());
            category.setParent(parent);
        }
        return CategoryMapper.INSTANCE.mapFromCategoryToCategoryAdminDetail(categoryRepository.save(category));
    }

    @Transactional
    public CategoryDetail update(Long id, CategoryRQ categoryRQ) {
        Category category = getById(id);
        if (StringUtils.isNotBlank(categoryRQ.getCode()) && categoryRepository.existsByCodeAndIgnoreId(categoryRQ.getCode(), category.getId())) {
            throw new BadRequestException("Mã danh mục đã tồn tại");
        }

        CategoryMapper.INSTANCE.merge(category, categoryRQ);
        if (categoryRQ.getParentId() != null) {
            Category parent = getById(categoryRQ.getParentId());
            category.setParent(parent);
        } else {
            category.setParent(null);
        }
        return CategoryMapper.INSTANCE.mapFromCategoryToCategoryAdminDetail(categoryRepository.save(category));
    }

    @Transactional
    public void delete(Long id) {
        Category category = getById(id);
        List<Category> categories = categoryRepository.findByParentId(id);
        categories.add(category);
        categories.forEach(item -> item.setIsDeleted(true));
        categoryRepository.persist(categories);
    }

    @Transactional
    public Pageable<CategoryDetail> getCategories(GetCategoryRQ getCategoryRQ) {
        PageRequest pageRequest = PageUtils.createPageRequest(getCategoryRQ);
        Specification<Category> specification = CategorySpecifications.builder()
                .isDeleted(getCategoryRQ.getIsDeleted())
                .withNameLike(getCategoryRQ.getName())
                .withCode(getCategoryRQ.getCode())
                .withOutParent(getCategoryRQ.getWithOutParent())
                .build();
        Pageable<Category> page = categoryRepository.findAll(specification, pageRequest);
        return PageUtils.createPageResponse(page.getItems(), page.getTotal(), CategoryMapper.INSTANCE::mapFromCategoryToCategoryAdminDetail);
    }

    @Transactional
    public Pageable<CategoryDetail> getActiveCategories(GetCategoryRQ getCategoryRQ) {
        PageRequest pageRequest = PageUtils.createPageRequest(getCategoryRQ);
        Specification<Category> specification = CategorySpecifications.builder()
                .isDeleted(false)
                .withCode(getCategoryRQ.getCode())
                .withNameLike(getCategoryRQ.getName())
                .withOutParent(getCategoryRQ.getWithOutParent())
                .build();
        Pageable<Category> page = categoryRepository.findAll(specification, pageRequest);
        return PageUtils.createPageResponse(page.getItems(), page.getTotal(), CategoryMapper.INSTANCE::mapFromCategoryToCategoryAdminDetail);
    }

    public CategoryDetail getCategoryRSById(Long id) {
        return CategoryMapper.INSTANCE.mapFromCategoryToCategoryAdminDetail(getById(id));
    }

    public CategoryDetail getActiveCategoryRSByCode(String code) {
        return CategoryMapper.INSTANCE.mapFromCategoryToCategoryAdminDetail(getActiveByCode(code));
    }

    public CategoryDetail getActiveCategoryRSById(Long id) {
        return CategoryMapper.INSTANCE.mapFromCategoryToCategoryAdminDetail(getActiveById(id));
    }

    public List<Category> getActiveByIdIn(Set<Long> ids) {
        return categoryRepository.findActiveByIdIn(ids);
    }

    private Category getById(Long id) {
        return categoryRepository.findByIdOptional(id).orElseThrow(() -> new EntityNotFoundException("Danh mục không tồn tại!"));
    }

    private Category getActiveById(Long id) {
        return categoryRepository.findActiveByIdOptional(id).orElseThrow(() -> new EntityNotFoundException("Danh mục không tồn tại!"));
    }

    private Category getActiveByCode(String code) {
        return categoryRepository.findByCodeAndIsDeletedOptional(code, false).orElseThrow(() -> new EntityNotFoundException("Danh mục không tồn tại!"));
    }

    private String generateCategoryCode(String categoryName) {
        String code = StringUtils.stripAccents(categoryName.toLowerCase().trim());
        return code.replaceAll(" ", "-");
    }
}
