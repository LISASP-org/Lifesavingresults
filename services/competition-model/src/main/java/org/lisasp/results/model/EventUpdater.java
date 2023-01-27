package org.lisasp.results.model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lisasp.results.base.api.type.EventType;
import org.lisasp.results.base.api.type.Gender;
import org.lisasp.results.base.api.type.InputValueType;
import org.lisasp.results.base.api.value.Round;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class EventUpdater {

    private final DatabaseUpdate<EventEntity> eventDatabaseUpdate;
    private final DatabaseUpdate<EntryEntity> entryDatabaseUpdate;
    private EventEntity event;

    private List<EntryUpdater> entries;

    private boolean used = false;
    private boolean modified = false;

    public void updateEvent(Consumer<EventEntity> updater) {
        used = true;

        String before = event.toString();
        updater.accept(event);
        String after = event.toString();
        if (!after.equals(before)) {
            modified = true;
            // repository.save(event);
        }
    }

    void save() {
        entries.forEach(e -> e.save());

        if (!used) {
            // repository.delete(event);
            eventDatabaseUpdate.delete(event);
        } else if (modified) {
            eventDatabaseUpdate.save(event);
        }
    }

    EventUpdater initialize(EventEntity event) {
        this.event = event;
        if (event.getEntries() == null) {
            entries = new ArrayList<>();
        } else {
            entries = event.getEntries().stream().map(e -> new EntryUpdater(entryDatabaseUpdate, e)).collect(Collectors.toList());
        }
        return this;
    }

    boolean matches(EventType eventType, String agegroup, Gender gender, String discipline, Round round, InputValueType inputValueType) {
        return event.matches(eventType, agegroup, gender, discipline, round, inputValueType);
    }

    public void updateEntry(String number, Consumer<EntryUpdater> updater) {
        EntryUpdater entryUpdater =  entries.stream().filter(e -> e.matches(number)).findFirst().orElseGet(() -> {
            EntryUpdater newEntryUpdater = new EntryUpdater(entryDatabaseUpdate, new EntryEntity(event));
            entries.add(newEntryUpdater);
            return newEntryUpdater;
        });
        updater.accept(entryUpdater);
    }
}
