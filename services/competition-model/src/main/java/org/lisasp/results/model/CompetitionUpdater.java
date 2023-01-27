package org.lisasp.results.model;

import lombok.RequiredArgsConstructor;
import org.lisasp.competition.api.exception.CompetitionNotFoundException;
import org.lisasp.competition.api.exception.NotFoundException;
import org.lisasp.results.base.api.type.EventType;
import org.lisasp.results.base.api.type.Gender;
import org.lisasp.results.base.api.type.InputValueType;
import org.lisasp.results.base.api.value.Round;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CompetitionUpdater {
    private final CompetitionRepository repository;
    private final EventRepository eventRepository;
    private final EntryRepository entryRepository;

    private CompetitionEntity competition;
    private List<EventUpdater> eventUpdaters;

    private DatabaseUpdate<EntryEntity> entryDatabaseUpdate;
    private DatabaseUpdate<EventEntity> eventDatabaseUpdate;

    CompetitionUpdater initialize(String id) throws NotFoundException {
        entryDatabaseUpdate = new DatabaseUpdate<>(entryRepository, null);
        eventDatabaseUpdate = new DatabaseUpdate<>(eventRepository, entryDatabaseUpdate);

        competition = repository.findById(id).orElseThrow(() -> new CompetitionNotFoundException(id));
        eventUpdaters = competition.getEvents().stream().map(e -> new EventUpdater(eventDatabaseUpdate, entryDatabaseUpdate).initialize(e)).collect(Collectors.toList());
        return this;
    }

    public void updateCompetition(Consumer<CompetitionEntity> updater) {
        String fixedUploadId = competition.getUploadId();
        updater.accept(competition);
        ensureUploadId(competition, fixedUploadId);
    }

    public void save() {
        repository.save(competition);
        eventUpdaters.forEach(u -> u.save());

        eventDatabaseUpdate.invokeUpdate();
    }

    private void ensureUploadId(CompetitionEntity entity, String fixedUploadId) {
        if (fixedUploadId != null && !fixedUploadId.isBlank()) {
            entity.setUploadId(fixedUploadId);
        } else if (entity.getUploadId() == null || entity.getUploadId().isBlank()) {
            entity.setUploadId(UUID.randomUUID().toString());
        }
    }

    public void updateEvent(EventType eventType, String agegroup, Gender gender, String discipline, Round round, InputValueType inputValueType, Consumer<EventUpdater> updater) {
        EventUpdater eventUpdater =  eventUpdaters.stream().filter(e -> e.matches(eventType, agegroup, gender, discipline, round, inputValueType)).findFirst().orElseGet(() -> {
            EventUpdater newEventUpdater = new EventUpdater(eventDatabaseUpdate, entryDatabaseUpdate).initialize(new EventEntity(competition));
            this.eventUpdaters.add(newEventUpdater);
            return newEventUpdater;
        });
        updater.accept(eventUpdater);
    }
}
