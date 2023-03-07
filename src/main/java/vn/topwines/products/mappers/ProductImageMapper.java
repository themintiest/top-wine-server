package vn.topwines.products.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.topwines.products.domain.ProductImageRS;
import vn.topwines.products.entity.ProductImage;

@Mapper(
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL
)
public interface ProductImageMapper {
    ProductImageMapper INSTANCE = Mappers.getMapper(ProductImageMapper.class);

    ProductImageRS mapProductImageToProductImageRS(ProductImage productImage);
}
