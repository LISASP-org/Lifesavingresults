package org.lisasp.results.model;

import lombok.RequiredArgsConstructor;
import org.lisasp.competition.api.dto.EventDto;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventService {

    private final EventRepository repository;

    private final EntityToDtoMapper mapper = Mappers.getMapper(EntityToDtoMapper.class);

    public EventDto[] findEvents(String competitionId) {
        return repository.findAllByCompetitionId(competitionId).stream().map(e -> mapper.entityToDto(e)).toArray(EventDto[]::new);
    }
}
