package org.lisasp.competition.results.service;

import org.lisasp.competition.results.api.CompetitionDto;
import org.lisasp.competition.results.api.EntryDto;
import org.lisasp.competition.results.api.EventDto;

// @Mapper
class EntityToDtoMapper {
    CompetitionDto entityToDto(CompetitionEntity entity) {
        return new CompetitionDto(entity.getId(), entity.getVersion(), entity.getUploadId(), entity.getName(), entity.getAcronym(), entity.getFrom(), entity.getTill());
    }

    EventDto entityToDto(EventEntity entity) {
        return new EventDto(entity.getId(), entity.getVersion(), entity.getAgegroup(), entity.getEventType(), entity.getGender(), entity.getDiscipline(), entity.getRound(), entity.getInputValueType());
    }

    EntryDto entityToDto(EntryEntity entity) {
        return new EntryDto(entity.getId(), entity.getVersion(), entity.getNumber(), entity.getName(), entity.getClub(), entity.getNationality(), entity.getTimeInMillis(), entity.getPlaceInHeat(), entity.getPenalties(), entity.getSwimmer(), entity.getStart());
    }
}

