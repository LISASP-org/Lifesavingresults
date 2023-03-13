package org.lisasp.competition.results.service;

import lombok.RequiredArgsConstructor;
import org.lisasp.competition.results.api.EventDto;
import org.lisasp.competition.results.api.exception.NotFoundException;

@RequiredArgsConstructor
public class EventService {

    private final CompetitionRepository competitionRepository;
    private final EventRepository repository;
    private final EntityToDtoMapper mapper = new EntityToDtoMapper();

    public EventDto[] findEvents(String competitionId) throws NotFoundException {
        if (!competitionRepository.existsById(competitionId)) {
            throw new NotFoundException("Competition", competitionId);
        }
        return repository.findAllByCompetitionId(competitionId).stream().map(e -> mapper.entityToDto(e)).toArray(EventDto[]::new);
    }
}
