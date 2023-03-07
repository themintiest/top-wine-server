package vn.topwines.product_type.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.topwines.product_type.domain.CreateProductTypeRQ;
import vn.topwines.product_type.domain.ProductTypeDetailRS;
import vn.topwines.product_type.domain.ProductTypeRS;
import vn.topwines.product_type.domain.ProductTypeSimpleRS;
import vn.topwines.product_type.domain.UpdateProductTypeRQ;
import vn.topwines.product_type.entity.ProductType;

@Mapper(
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
)
public interface ProductTypeMapper {
    ProductTypeMapper INSTANCE = Mappers.getMapper(ProductTypeMapper.class);

    ProductType mapCreateProductTypeRQToProductType(CreateProductTypeRQ createProductTypeRQ);

    ProductTypeRS mapProductTypeToProductTypeRS(ProductType productType);

    ProductTypeSimpleRS mapProductTypeToProductTypeSimpleRS(ProductType productType);

    ProductTypeDetailRS mapProductTypeToProductTypeDetailRS(ProductType productType);

    @Mapping(target = "id", ignore = true)
    void merge(@MappingTarget ProductType target, UpdateProductTypeRQ source);
}
