package org.lisasp.results.model;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
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
import java.util.stream.Stream;

@RequiredArgsConstructor
public class CompetitionUpdater {
    private final CompetitionRepository repository;
    private final EventRepository eventRepository;
    private final EntryRepository entryRepository;

    private CompetitionEntity competition;
    private List<EventUpdater> eventUpdaters;

    private Changes<EntryEntity> entryChanges;
    private Changes<EventEntity> eventChanges;

    CompetitionUpdater initialize(String id) throws NotFoundException {
        entryChanges = new Changes<>(entryRepository, null);
        eventChanges = new Changes<>(eventRepository, entryChanges);

        competition = repository.findById(id).orElseThrow(() -> new CompetitionNotFoundException(id));
        eventUpdaters = getEventStream().map(e -> new EventUpdater(eventChanges, entryChanges).initialize(e)).collect(Collectors.toList());
        return this;
    }

    private Stream<EventEntity> getEventStream() {
        if (competition.getEvents() == null) {
            return Stream.of();
        }
        return competition.getEvents().stream();
    }

    public void updateCompetition(Consumer<CompetitionEntity> updater) {
        String fixedUploadId = competition.getUploadId();
        updater.accept(competition);
        ensureUploadId(competition, fixedUploadId);
    }

    public void save() {
        repository.save(competition);
        eventUpdaters.forEach(u -> u.save());

        eventChanges.invokeUpdate();
    }

    private void ensureUploadId(CompetitionEntity entity, String fixedUploadId) {
        if (fixedUploadId != null && !fixedUploadId.isBlank()) {
            entity.setUploadId(fixedUploadId);
        } else if (entity.getUploadId() == null || entity.getUploadId().isBlank()) {
            entity.setUploadId(UUID.randomUUID().toString());
        }
    }

    public void updateEvent(EventType eventType, String agegroup, Gender gender, String discipline, Round round, InputValueType inputValueType, Consumer<EventUpdater> updater) {
        EventUpdater eventUpdater = eventUpdaters
                .stream()
                .filter(e -> e.matches(eventType, agegroup, gender, discipline, round, inputValueType))
                .findFirst()
                .orElseGet(() -> {
                    return setupNewEventUpdater(eventType, agegroup, gender, discipline, round, inputValueType);
                });
        updater.accept(eventUpdater);
    }

    @NotNull
    private EventUpdater setupNewEventUpdater(EventType eventType, String agegroup, Gender gender, String discipline, Round round, InputValueType inputValueType) {
        EventUpdater newEventUpdater = new EventUpdater(eventChanges, entryChanges)
                .initialize(new EventEntity(competition));
        newEventUpdater.updateEvent(eventEntity -> {
            eventEntity.setEventType(eventType);
            eventEntity.setAgegroup(agegroup);
            eventEntity.setGender(gender);
            eventEntity.setDiscipline(discipline);
            eventEntity.setRound(round);
            eventEntity.setInputValueType(inputValueType);
        });
        this.eventUpdaters.add(newEventUpdater);
        return newEventUpdater;
    }
}
