package vn.topwines.regions.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.topwines.regions.domain.CreateRegionRQ;
import vn.topwines.regions.domain.RegionDetailRS;
import vn.topwines.regions.domain.RegionRS;
import vn.topwines.regions.domain.RegionSimpleRS;
import vn.topwines.regions.domain.UpdateRegionRQ;
import vn.topwines.regions.entity.Region;

@Mapper(
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
)
public interface RegionMapper {
    RegionMapper INSTANCE = Mappers.getMapper(RegionMapper.class);

    Region mapCreateRegionRQToRegion(CreateRegionRQ createRegionRQ);

    RegionRS mapRegionToRegionRS(Region region);

    RegionSimpleRS mapRegionToRegionSimpleRS(Region region);

    RegionDetailRS mapRegionToRegionDetailRS(Region region);

    @Mapping(target = "id", ignore = true)
    void merge(@MappingTarget Region target, UpdateRegionRQ source);
}
