package org.lisasp.results.competition.service;

import org.lisasp.results.competition.api.CompetitionDto;
import org.lisasp.results.competition.api.EntryDto;
import org.lisasp.results.competition.api.EventDto;
import org.lisasp.results.competition.model.CompetitionEntity;
import org.lisasp.results.competition.model.EntryEntity;
import org.lisasp.results.competition.model.EventEntity;
import org.mapstruct.Mapper;

@Mapper
interface EntityToDtoMapper {
    CompetitionDto entityToDto(CompetitionEntity entity);
    EventDto entityToDto(EventEntity entity);
    EntryDto entityToDto(EntryEntity entity);
}

