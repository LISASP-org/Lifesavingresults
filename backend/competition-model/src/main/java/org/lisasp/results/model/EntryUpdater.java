package org.lisasp.results.model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public class EntryUpdater {
    private final DatabaseUpdate<EntryEntity> entryDatabaseUpdate;
    private final EntryEntity entry;

    private boolean used = false;
    private boolean modified = true;

    public void updateEntry(Consumer<EntryEntity> updater) {
        used = true;

        String before = entry.toString();
        updater.accept(entry);
        entry.fixNull();
        String after = entry.toString();
        if (!after.equals(before)) {
            modified = true;
        }
    }

    void save() {
        if (!used) {
            entryDatabaseUpdate.delete(entry);
        } else if (modified) {
            entryDatabaseUpdate.save(entry);
        }
    }

    boolean matches(String number) {
        return entry.matches(number);
    }
}
