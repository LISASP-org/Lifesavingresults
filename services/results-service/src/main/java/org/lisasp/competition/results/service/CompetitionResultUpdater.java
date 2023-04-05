package org.lisasp.competition.results.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.lisasp.competition.base.api.exception.CompetitionNotFoundException;
import org.lisasp.competition.base.api.exception.NotFoundException;
import org.lisasp.competition.results.api.EntryChangeListener;
import org.lisasp.competition.results.api.imports.Competition;
import org.lisasp.competition.results.api.imports.Event;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
class CompetitionResultUpdater {
    private final CompetitionResultRepository repository;
    private final EventResultRepository eventResultRepository;
    private final EntryResultRepository entryResultRepository;

    private CompetitionResultEntity competition;
    private List<EventResultUpdater> eventResultUpdaters;

    private Changes<EntryResultEntity> entryChanges;
    private Changes<EventResultEntity> eventChanges;
    private final EntryResultChangeListener changeListener = new EntryResultChangeListener();

    CompetitionResultUpdater initialize(String id, EntryChangeListener[] listeners) throws NotFoundException {
        changeListener.setListeners(listeners);

        entryChanges = new Changes<>(entryResultRepository, null, changeListener);
        eventChanges = new Changes<>(eventResultRepository, entryChanges, null);

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

    public void updateCompetition(Competition importedCompetition) {
        if (importedCompetition.events() != null) {
            for (Event event : importedCompetition.events()) {
                updateEvent(event);
            }
        }
    }

    public void checkChanges() {
        eventResultUpdaters.forEach(EventResultUpdater::checkChanges);

        eventChanges.invokeUpdate();
        changeListener.notifyListeners();
    }

    public void updateEvent(Event event) {
        eventResultUpdaters.stream()
                           .filter(e -> e.matches(event.eventType(),
                                                  event.agegroup(),
                                                  event.gender(),
                                                  event.discipline(),
                                                  event.round(),
                                                  event.inputValueType()))
                           .findFirst()
                           .orElseGet(this::createNewEventUpdater)
                           .updateEvent(event);
    }

    @NotNull
    private EventResultUpdater createNewEventUpdater() {
        EventResultUpdater newEventResultUpdater = new EventResultUpdater(eventChanges, entryChanges).initialize(new EventResultEntity(competition));
        eventResultUpdaters.add(newEventResultUpdater);
        return newEventResultUpdater;
    }
}
