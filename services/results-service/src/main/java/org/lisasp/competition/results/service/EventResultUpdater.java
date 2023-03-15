package org.lisasp.competition.results.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lisasp.competition.base.api.type.EventType;
import org.lisasp.competition.base.api.type.Gender;
import org.lisasp.competition.base.api.type.InputValueType;
import org.lisasp.competition.results.api.value.Round;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class EventResultUpdater {

    private final Changes<EventResultEntity> eventChanges;
    private final Changes<EntryResultEntity> entryChanges;
    private EventResultEntity event;

    private List<EntryResultUpdater> entries;

    private boolean used = false;
    private boolean modified = false;

    public void updateEvent(Consumer<EventResultEntity> updater) {
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

    EventResultUpdater initialize(EventResultEntity event) {
        this.event = event;
        if (event.getEntries() == null) {
            entries = new ArrayList<>();
        } else {
            entries = getEntityStream(event).map(e -> new EntryResultUpdater(entryChanges, e)).collect(Collectors.toList());
        }
        return this;
    }

    private static Stream<EntryResultEntity> getEntityStream(EventResultEntity event) {
        if (event.getEntries() == null) {
            return Stream.of();
        }
        return event.getEntries().stream();
    }

    boolean matches(EventType eventType, String agegroup, Gender gender, String discipline, Round round, InputValueType inputValueType) {
        return event.matches(eventType, agegroup, gender, discipline, round, inputValueType);
    }

    public void updateEntry(String number, Consumer<EntryResultUpdater> updater) {
        EntryResultUpdater entryResultUpdater = entries.stream().filter(e -> e.matches(number)).findFirst().orElseGet(() -> {
            EntryResultUpdater newEntryResultUpdater = new EntryResultUpdater(entryChanges, new EntryResultEntity(event));
            newEntryResultUpdater.updateEntry(entryEntity -> entryEntity.setNumber(number));
            entries.add(newEntryResultUpdater);
            return newEntryResultUpdater;
        });
        updater.accept(entryResultUpdater);
    }
}
