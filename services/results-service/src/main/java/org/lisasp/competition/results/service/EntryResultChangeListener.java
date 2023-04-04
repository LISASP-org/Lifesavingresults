package org.lisasp.competition.results.service;

import lombok.RequiredArgsConstructor;
import org.lisasp.competition.base.api.ChangeType;
import org.lisasp.competition.results.api.EntryChangeListener;
import org.lisasp.competition.results.api.EntryChangedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class EntryResultChangeListener implements ChangeListener<EntryResultEntity> {

    private final EntityToDtoMapper mapper = new EntityToDtoMapper();
    private final List<EntryChangedEvent> events = new ArrayList<>();
    private List<EntryChangeListener> listeners = new ArrayList<>();

    @Override
    public void changed(Collection<EntryResultEntity> entities) {
        for (EntryResultEntity entry : entities) {
            addEventForEntry(entry.getVersion() == 1 ? ChangeType.Created : ChangeType.Updated, entry);
        }
    }

    private void addEventForEntry(ChangeType changeType, EntryResultEntity entry) {
        EventResultEntity event = entry.getEvent();
        CompetitionResultEntity competition = event.getCompetition();
        events.add(new EntryChangedEvent(changeType, mapper.entityToDto(competition), mapper.entityToDto(event), mapper.entityToDto(entry)));
    }

    @Override
    public void deleted(Collection<EntryResultEntity> entities) {
        for (EntryResultEntity entry : entities) {
            addEventForEntry(ChangeType.Deleted, entry);
        }
    }

    public void notifyListeners() {
        events.forEach(event -> listeners.forEach(l -> l.changed(event)));
    }

    public void setListeners(EntryChangeListener[] listeners) {
        this.listeners = Arrays.asList(listeners);
    }
}
