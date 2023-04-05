package org.lisasp.competition.results.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lisasp.competition.results.api.imports.Entry;

@Slf4j
@RequiredArgsConstructor
class EntryResultUpdater {

    private final ObjectMapper mapper = new ObjectMapper();

    private final Changes<EntryResultEntity> entryChanges;
    private final EntryResultEntity entry;

    private boolean used = false;
    private boolean modified = false;

    void updateEntry(Entry importedEntry) {
        used = true;

        String before = serialize();
        updateFields(importedEntry);
        if (mustBeSaved(before)) {
            modified = true;
        }
    }

    private String serialize() {
        try {
            return mapper.writeValueAsString(entry);
        } catch (JsonProcessingException e) {
            return entry.toString();
        }
    }

    private void updateFields(Entry importedEntry) {
        entry.setNumber(importedEntry.number());
        entry.setSwimmer(importedEntry.swimmer());
        entry.setStart(importedEntry.start());
        entry.setSplitTimes(importedEntry.splitTimes());
        entry.setPlaceInHeat(importedEntry.placeInHeat());
        entry.setPenalties(importedEntry.penalties());
        entry.setNationality(importedEntry.nationality());
        entry.setName(importedEntry.name());
        entry.setClub(importedEntry.club());
        entry.setTimeInMillis(importedEntry.timeInMillis());
    }

    private boolean mustBeSaved(String before) {
        return entry.isNew() || idChanged(before);
    }

    private boolean idChanged(String before) {
        return !serialize().equals(before);
    }

    void checkChanges() {
        if (!used) {
            entryChanges.delete(entry);
        } else if (modified) {
            entryChanges.save(entry);
        }
    }

    boolean matches(String number) {
        return entry.matches(number);
    }
}
