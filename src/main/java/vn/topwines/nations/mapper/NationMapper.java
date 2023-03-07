package vn.topwines.nations.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.topwines.nations.domain.CreateNationRQ;
import vn.topwines.nations.domain.NationDetailRS;
import vn.topwines.nations.domain.NationRS;
import vn.topwines.nations.domain.NationSimpleRS;
import vn.topwines.nations.domain.UpdateNationRQ;
import vn.topwines.nations.entity.Nation;

@Mapper(
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
)
public interface NationMapper {
    NationMapper INSTANCE = Mappers.getMapper(NationMapper.class);

    Nation mapCreateNationRQToNation(CreateNationRQ createNationRQ);

    NationRS mapNationToNationRS(Nation nation);

    NationSimpleRS mapNationToNationSimpleRS(Nation nation);

    NationDetailRS mapNationToNationDetailRS(Nation nation);

    @Mapping(target = "id", ignore = true)
    void merge(@MappingTarget Nation target, UpdateNationRQ source);
}
