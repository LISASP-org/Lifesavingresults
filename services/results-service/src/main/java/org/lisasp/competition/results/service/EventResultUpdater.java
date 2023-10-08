package org.lisasp.competition.results.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lisasp.competition.base.api.type.EventType;
import org.lisasp.competition.base.api.type.Gender;
import org.lisasp.competition.base.api.type.InputValueType;
import org.lisasp.competition.results.api.imports.Entry;
import org.lisasp.competition.results.api.imports.Event;
import org.lisasp.competition.results.api.value.Round;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
class EventResultUpdater {

    private final ObjectMapper mapper = new ObjectMapper();

    private final Changes<EventResultEntity> eventChanges;
    private final Changes<EntryResultEntity> entryChanges;
    private EventResultEntity event;

    private List<EntryResultUpdater> entries;

    private boolean used = false;
    private boolean modified = false;

    void updateEvent(Event importedEvent) {
        used = true;

        String before = serialize(event);
        updateFields(importedEvent);
        if (mustBeSaved(before)) {
            modified = true;
        }

        if (importedEvent.entries() != null) {
            for (Entry entry : importedEvent.entries()) {
                updateEntry(entry);
            }
        }
    }

    private String serialize(EventResultEntity event) {
        try {
            return mapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            return event.toString();
        }
    }

    private void updateFields(Event importedEvent) {
        event.setEventType(importedEvent.eventType());
        event.setAgegroup(importedEvent.agegroup());
        event.setGender(importedEvent.gender());
        event.setDiscipline(importedEvent.discipline());
        event.setRound(importedEvent.round());
        event.setInputValueType(importedEvent.inputValueType());
        event.setCourseType(importedEvent.courseType());
        event.setDate(importedEvent.date() != null ? importedEvent.date() : event.getCompetition().getFrom());
    }

    private boolean mustBeSaved(String before) {
        return event.isNew() || isChanged(before);
    }

    private boolean isChanged(String before) {
        return !serialize(event).equals(before);
    }

    void checkChanges() {
        entries.forEach(EntryResultUpdater::checkChanges);

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
        return event.getEntries().stream();
    }

    boolean matches(EventType eventType, String agegroup, Gender gender, String discipline, Round round, InputValueType inputValueType) {
        return event.matches(eventType, agegroup, gender, discipline, round, inputValueType);
    }

    private void updateEntry(Entry entry) {
        EntryResultUpdater entryResultUpdater = entries.stream().filter(e -> e.matches(entry.number())).findFirst().orElseGet(() -> {
            EntryResultUpdater newEntryResultUpdater = new EntryResultUpdater(entryChanges, new EntryResultEntity(event));
            entries.add(newEntryResultUpdater);
            return newEntryResultUpdater;
        });
        entryResultUpdater.updateEntry(entry);
    }
}
