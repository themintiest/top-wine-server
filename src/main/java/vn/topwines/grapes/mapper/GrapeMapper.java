package vn.topwines.grapes.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.topwines.grapes.domain.CreateGrapeRQ;
import vn.topwines.grapes.domain.GrapeDetailRS;
import vn.topwines.grapes.domain.GrapeRS;
import vn.topwines.grapes.domain.GrapeSimpleRS;
import vn.topwines.grapes.domain.UpdateGrapeRQ;
import vn.topwines.grapes.entity.Grape;

@Mapper(
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
)
public interface GrapeMapper {
    GrapeMapper INSTANCE = Mappers.getMapper(GrapeMapper.class);

    Grape mapCreateGrapeRQToGrape(CreateGrapeRQ createGrapeRQ);

    GrapeRS mapGrapeToGrapeRS(Grape grape);

    GrapeSimpleRS mapGrapeToGrapeSimpleRS(Grape grape);

    GrapeDetailRS mapGrapeToGrapeDetailRS(Grape grape);

    @Mapping(target = "id", ignore = true)
    void merge(@MappingTarget Grape target, UpdateGrapeRQ source);
}
