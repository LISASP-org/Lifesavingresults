package org.lisasp.results.model;

import org.lisasp.results.competition.api.CompetitionDto;
import org.lisasp.results.competition.api.EntryDto;
import org.lisasp.results.competition.api.EventDto;
import org.mapstruct.Mapper;

@Mapper
interface EntityToDtoMapper {
    CompetitionDto entityToDto(CompetitionEntity entity);
    EventDto entityToDto(EventEntity entity);
    EntryDto entityToDto(EntryEntity entity);
}

