package org.lisasp.results.competition.service;

import lombok.RequiredArgsConstructor;
import org.lisasp.results.competition.api.EventDto;
import org.lisasp.results.competition.api.exception.NotFoundException;
import org.lisasp.results.competition.model.CompetitionRepository;
import org.lisasp.results.competition.model.EventRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventService {

    private final EventRepository repository;
    private final CompetitionRepository competitionRepository;

    private final EntityToDtoMapper mapper = Mappers.getMapper(EntityToDtoMapper.class);

    public EventDto[] findEvents(String competitionId) throws NotFoundException {
        if (!competitionRepository.existsById(competitionId)) {
            throw new NotFoundException("Competition", competitionId);
        }
        return repository.findAllByCompetitionId(competitionId).stream().map(e -> mapper.entityToDto(e)).toArray(EventDto[]::new);
    }
}
