package org.lisasp.results.model;

import lombok.RequiredArgsConstructor;
import org.lisasp.competition.api.dto.EntryDto;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EntryService {

    private final EntryRepository repository;

    private final EntityToDtoMapper mapper = Mappers.getMapper(EntityToDtoMapper.class);

    public EntryDto[] findEntries(String eventId) {
        return repository.findAllByEventId(eventId).stream().map(e -> mapper.entityToDto(e)).toArray(EntryDto[]::new);
    }
}
