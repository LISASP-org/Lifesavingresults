package org.lisasp.competition.results.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.lisasp.competition.base.api.exception.CompetitionNotFoundException;
import org.lisasp.competition.base.api.exception.NotFoundException;
import org.lisasp.competition.base.api.type.EventType;
import org.lisasp.competition.base.api.type.Gender;
import org.lisasp.competition.base.api.type.InputValueType;
import org.lisasp.competition.results.api.value.Round;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ResultUpdater {
    private final CompetitionResultRepository repository;
    private final EventRepository eventRepository;
    private final EntryRepository entryRepository;

    private CompetitionResultEntity competition;
    private List<EventResultUpdater> eventResultUpdaters;

    private Changes<EntryResultEntity> entryChanges;
    private Changes<EventResultEntity> eventChanges;

    ResultUpdater initialize(String id) throws NotFoundException {
        entryChanges = new Changes<>(entryRepository, null);
        eventChanges = new Changes<>(eventRepository, entryChanges);

        competition = repository.findById(id).orElseThrow(() -> new CompetitionNotFoundException(id));
        eventResultUpdaters = getEventStream().map(e -> new EventResultUpdater(eventChanges, entryChanges).initialize(e)).collect(Collectors.toList());
        return this;
    }

    private Stream<EventResultEntity> getEventStream() {
        if (competition.getEvents() == null) {
            return Stream.of();
        }
        return competition.getEvents().stream();
    }

    public void updateCompetition(Consumer<CompetitionResultEntity> updater) {
        String fixedUploadId = competition.getUploadId();
        updater.accept(competition);
        ensureUploadId(competition, fixedUploadId);
    }

    public void save() {
        repository.save(competition);
        eventResultUpdaters.forEach(u -> u.save());

        eventChanges.invokeUpdate();
    }

    private void ensureUploadId(CompetitionResultEntity entity, String fixedUploadId) {
        if (fixedUploadId != null && !fixedUploadId.isBlank()) {
            entity.setUploadId(fixedUploadId);
        } else if (entity.getUploadId() == null || entity.getUploadId().isBlank()) {
            entity.setUploadId(UUID.randomUUID().toString());
        }
    }

    public void updateEvent(EventType eventType, String agegroup, Gender gender, String discipline, Round round, InputValueType inputValueType, Consumer<EventResultUpdater> updater) {
        EventResultUpdater eventResultUpdater = eventResultUpdaters
                .stream()
                .filter(e -> e.matches(eventType, agegroup, gender, discipline, round, inputValueType))
                .findFirst()
                .orElseGet(() -> setupNewEventUpdater(eventType, agegroup, gender, discipline, round, inputValueType));
        updater.accept(eventResultUpdater);
    }

    @NotNull
    private EventResultUpdater setupNewEventUpdater(EventType eventType, String agegroup, Gender gender, String discipline, Round round, InputValueType inputValueType) {
        EventResultUpdater newEventResultUpdater = new EventResultUpdater(eventChanges, entryChanges)
                .initialize(new EventResultEntity(competition));
        newEventResultUpdater.updateEvent(eventEntity -> {
            eventEntity.setEventType(eventType);
            eventEntity.setAgegroup(agegroup);
            eventEntity.setGender(gender);
            eventEntity.setDiscipline(discipline);
            eventEntity.setRound(round);
            eventEntity.setInputValueType(inputValueType);
        });
        this.eventResultUpdaters.add(newEventResultUpdater);
        return newEventResultUpdater;
    }
}
