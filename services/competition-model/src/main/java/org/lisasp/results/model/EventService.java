package org.lisasp.results.model;

import lombok.RequiredArgsConstructor;
import org.lisasp.results.competition.api.EventDto;
import org.lisasp.results.competition.api.exception.NotFoundException;

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
