package org.lisasp.competition.results.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public class EntryUpdater {
    private final Changes<EntryEntity> entryChanges;
    private final EntryEntity entry;

    private boolean used = false;
    private boolean modified = true;

    public void updateEntry(Consumer<EntryEntity> updater) {
        used = true;

        String before = entry.toString();
        updater.accept(entry);
        entry.fixNull();
        if (mustBeSaved(before)) {
            modified = true;
        }
    }

    private boolean mustBeSaved(String before) {
        return entry.isNew() || !entry.toString().equals(before);
    }

    void save() {
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
