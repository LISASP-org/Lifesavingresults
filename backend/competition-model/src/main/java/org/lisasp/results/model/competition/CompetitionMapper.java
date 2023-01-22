package org.lisasp.results.model.competition;

import org.lisasp.results.api.dto.CompetitionDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CompetitionMapper {
    CompetitionDto entityToDto(CompetitionEntity entity);
}

