package vn.topwines.brands.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.topwines.brands.domain.BrandDetailRS;
import vn.topwines.brands.domain.BrandRS;
import vn.topwines.brands.domain.BrandSimpleRS;
import vn.topwines.brands.domain.CreateBrandRQ;
import vn.topwines.brands.domain.UpdateBrandRQ;
import vn.topwines.brands.entity.Brand;

@Mapper(
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
)
public interface BrandMapper {
    BrandMapper INSTANCE = Mappers.getMapper(BrandMapper.class);

    Brand mapCreateBrandRQToBrand(CreateBrandRQ createBrandRQ);

    BrandRS mapBrandToBrandRS(Brand brand);

    BrandSimpleRS mapBrandToBrandSimpleRS(Brand brand);

    BrandDetailRS mapBrandToBrandDetailRS(Brand brand);

    @Mapping(target = "id", ignore = true)
    void merge(@MappingTarget Brand target, UpdateBrandRQ source);
}
