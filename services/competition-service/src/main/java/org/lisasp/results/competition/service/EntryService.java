package org.lisasp.results.competition.service;

import lombok.RequiredArgsConstructor;
import org.lisasp.results.competition.api.EntryDto;
import org.lisasp.results.competition.api.exception.NotFoundException;
import org.lisasp.results.competition.model.EntryRepository;
import org.lisasp.results.competition.model.EventRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EntryService {

    private final EntryRepository repository;
    private final EventRepository eventRepository;

    private final EntityToDtoMapper mapper = Mappers.getMapper(EntityToDtoMapper.class);

    public EntryDto[] findEntries(String competitionId, String eventId) throws NotFoundException {
        if (!eventRepository.existsByCompetitionIdAndId(competitionId, eventId)) {
            throw new NotFoundException("Event", eventId);
        }
        return repository.findAllByEventId(eventId).stream().map(e -> mapper.entityToDto(e)).toArray(EntryDto[]::new);
    }
}
