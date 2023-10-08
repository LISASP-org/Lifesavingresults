package org.lisasp.competition.results.service;

import lombok.RequiredArgsConstructor;
import org.lisasp.competition.base.api.exception.CompetitionNotFoundException;
import org.lisasp.competition.base.api.exception.InvalidDataException;
import org.lisasp.competition.base.api.exception.NotFoundException;
import org.lisasp.competition.results.api.CompetitionDto;
import org.lisasp.competition.results.api.EntryChangeListener;
import org.lisasp.competition.results.api.EntryDto;
import org.lisasp.competition.results.api.EventDto;
import org.lisasp.competition.results.api.imports.Competition;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
public class ResultService {

    private final CompetitionResultRepository competitionResultRepository;
    private final EventResultRepository eventResultRepository;
    private final EntryResultRepository entryResultRepository;

    private final EntityToDtoMapper mapper = new EntityToDtoMapper();

    private final List<EntryChangeListener> listeners = new ArrayList<>();

    public ResultService register(EntryChangeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
        return this;
    }

    public void addOrUpdate(org.lisasp.competition.api.CompetitionDto competitionDto) {
        CompetitionResultEntity competition = competitionResultRepository.findById(competitionDto.id()).orElseGet(() -> {
            CompetitionResultEntity entity = new CompetitionResultEntity(competitionDto.id());
            entity.setUploadId(UUID.randomUUID().toString());
            return entity;
        });

        competition.setName(competitionDto.name());
        competition.setAcronym(competitionDto.acronym());
        competition.setFrom(competitionDto.from());
        competition.setTill(competitionDto.till());

        competitionResultRepository.save(competition);
    }

    public void delete(String competitionId) {
        List<String> eventIds = eventResultRepository.findAllEventIdByCompetitionId(competitionId);
        List<String> entryIds = entryResultRepository.findAllEntryIdByEventId(eventIds);

        entryResultRepository.deleteAllById(entryIds);
        eventResultRepository.deleteAllById(eventIds);
        competitionResultRepository.deleteById(competitionId);
    }

    public CompetitionDto findCompetition(String id) throws NotFoundException {
        CompetitionResultEntity entity = competitionResultRepository.findById(id).orElseThrow(() -> new NotFoundException("Competition", id));
        return mapper.entityToDto(entity);
    }

    public CompetitionDto[] findCompetitions() {
        return StreamSupport.stream(competitionResultRepository.findAll().spliterator(), false).map(e -> mapper.entityToDto(e)).toArray(CompetitionDto[]::new);
    }

    public EventDto findEvent(String id) throws NotFoundException {
        EventResultEntity entity = eventResultRepository.findById(id).orElseThrow(() -> new NotFoundException("Event", id));
        return mapper.entityToDto(entity);
    }

    public EventDto[] findEvents(String competitionId) throws NotFoundException {
        if (!competitionResultRepository.existsById(competitionId)) {
            throw new NotFoundException("Competition", competitionId);
        }
        return eventResultRepository.findAllByCompetitionId(competitionId).stream().map(e -> mapper.entityToDto(e)).toArray(EventDto[]::new);
    }

    public EntryDto[] findEntries(String eventId) throws NotFoundException {
        if (!eventResultRepository.existsById(eventId)) {
            throw new NotFoundException("Event", eventId);
        }
        return entryResultRepository.findAllByEventId(eventId).stream().map(e -> mapper.entityToDto(e)).toArray(EntryDto[]::new);
    }

    public String getCompetitionIdByUploadId(String uploadId) throws CompetitionNotFoundException {
        CompetitionResultEntity entity = competitionResultRepository.findByUploadId(uploadId).orElseThrow(() -> new CompetitionNotFoundException(uploadId));
        return entity.getId();
    }

    public void update(String id, Competition competition) throws NotFoundException, InvalidDataException {
        if (id == null) {
            throw new InvalidDataException("Id must not be null");
        }
        if (id.isBlank()) {
            throw new InvalidDataException("Id must not be empty or all blanks");
        }
        if (competition == null) {
            throw new InvalidDataException("Competition must not be null");
        }
        CompetitionResultUpdater competitionResultUpdater = new CompetitionResultUpdater(competitionResultRepository, eventResultRepository, entryResultRepository).initialize(id, listeners.toArray(EntryChangeListener[]::new));
        competitionResultUpdater.updateCompetition(competition);
        competitionResultUpdater.checkChanges();
    }

    public String getUploadId(String competitionId) throws CompetitionNotFoundException {
        CompetitionResultEntity entity = competitionResultRepository.findById(competitionId).orElseThrow(() -> new CompetitionNotFoundException(competitionId));
        return entity.getUploadId();
    }
}
