package org.lisasp.results.model;

import org.lisasp.competition.api.dto.CompetitionDto;
import org.lisasp.competition.api.dto.EntryDto;
import org.lisasp.competition.api.dto.EventDto;
import org.mapstruct.Mapper;

@Mapper
interface EntityToDtoMapper {
    CompetitionDto entityToDto(CompetitionEntity entity);
    EventDto entityToDto(EventEntity entity);
    EntryDto entityToDto(EntryEntity entity);
}

