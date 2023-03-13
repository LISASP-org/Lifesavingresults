package org.lisasp.competition.results.service;

import lombok.RequiredArgsConstructor;
import org.lisasp.competition.results.api.EntryDto;
import org.lisasp.competition.results.api.exception.NotFoundException;

@RequiredArgsConstructor
public class EntryService {

    private final EventRepository eventRepository;
    private final EntryRepository repository;

    private final EntityToDtoMapper mapper = new EntityToDtoMapper();

    public EntryDto[] findEntries(String competitionId, String eventId) throws NotFoundException {
        if (!eventRepository.existsByCompetitionIdAndId(competitionId, eventId)) {
            throw new NotFoundException("Event", eventId);
        }
        return repository.findAllByEventId(eventId).stream().map(e -> mapper.entityToDto(e)).toArray(EntryDto[]::new);
    }
}
