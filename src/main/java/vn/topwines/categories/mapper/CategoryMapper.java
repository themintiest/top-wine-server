package vn.topwines.categories.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.topwines.categories.domain.CategoryDetail;
import vn.topwines.categories.domain.CategoryRQ;
import vn.topwines.categories.domain.CategorySimple;
import vn.topwines.categories.entity.Category;


@Mapper(
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
)
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDetail mapFromCategoryToCategoryAdminDetail(Category category);

    CategorySimple mapFromCategoryToCategoryAdminSimple(Category category);

    Category mapFromCategoryRQToCategory(CategoryRQ categoryRQ);

    @Mapping(target = "id", ignore = true)
    void merge(@MappingTarget Category target, CategoryRQ source);
}
