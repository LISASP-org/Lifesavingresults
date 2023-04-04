package org.lisasp.competition.service;

import org.lisasp.competition.api.CompetitionDto;

class EntityToDtoMapper {
    CompetitionDto entityToDto(CompetitionEntity entity) {
        return new CompetitionDto(entity.getId(),
                                  entity.getVersion(),
                                  entity.getName(),
                                  entity.getAcronym(),
                                  entity.getFrom(),
                                  entity.getTill());
    }
}

