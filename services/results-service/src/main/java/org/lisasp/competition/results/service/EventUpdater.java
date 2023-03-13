package org.lisasp.competition.results.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lisasp.competition.results.api.type.EventType;
import org.lisasp.competition.results.api.type.Gender;
import org.lisasp.competition.results.api.type.InputValueType;
import org.lisasp.competition.results.api.value.Round;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class EventUpdater {

    private final Changes<EventEntity> eventChanges;
    private final Changes<EntryEntity> entryChanges;
    private EventEntity event;

    private List<EntryUpdater> entries;

    private boolean used = false;
    private boolean modified = false;

    public void updateEvent(Consumer<EventEntity> updater) {
        used = true;

        String before = event.toString();
        updater.accept(event);
        if (mustBeSaved(before)) {
            modified = true;
        }
    }

    private boolean mustBeSaved(String before) {
        return event.isNew() || !event.toString().equals(before);
    }

    void save() {
        entries.forEach(e -> e.save());

        if (!used) {
            eventChanges.delete(event);
        } else if (modified) {
            eventChanges.save(event);
        }
    }

    EventUpdater initialize(EventEntity event) {
        this.event = event;
        if (event.getEntries() == null) {
            entries = new ArrayList<>();
        } else {
            entries = getEntityStream(event).map(e -> new EntryUpdater(entryChanges, e)).collect(Collectors.toList());
        }
        return this;
    }

    private static Stream<EntryEntity> getEntityStream(EventEntity event) {
        if (event.getEntries() == null) {
            return Stream.of();
        }
        return event.getEntries().stream();
    }

    boolean matches(EventType eventType, String agegroup, Gender gender, String discipline, Round round, InputValueType inputValueType) {
        return event.matches(eventType, agegroup, gender, discipline, round, inputValueType);
    }

    public void updateEntry(String number, Consumer<EntryUpdater> updater) {
        EntryUpdater entryUpdater = entries.stream().filter(e -> e.matches(number)).findFirst().orElseGet(() -> {
            EntryUpdater newEntryUpdater = new EntryUpdater(entryChanges, new EntryEntity(event));
            newEntryUpdater.updateEntry(entryEntity -> entryEntity.setNumber(number));
            entries.add(newEntryUpdater);
            return newEntryUpdater;
        });
        updater.accept(entryUpdater);
    }
}
