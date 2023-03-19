package org.lisasp.competition.results.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lisasp.competition.base.api.EventType;
import org.lisasp.competition.results.api.TimeChangeListener;
import org.lisasp.competition.results.api.TimeChangedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class EntryResultChangeListener implements ChangeListener<EntryResultEntity> {

    private final EntityToDtoMapper mapper = new EntityToDtoMapper();
    private final List<TimeChangedEvent> events = new ArrayList<>();
    private List<TimeChangeListener> listeners = new ArrayList<>();

    @Override
    public void changed(Collection<EntryResultEntity> entities) {
        for (EntryResultEntity entry : entities) {
            addEventForEntry(entry.getVersion() == 1 ? EventType.Created : EventType.Updated, entry);
        }
    }

    private void addEventForEntry(EventType eventType, EntryResultEntity entry) {
        EventResultEntity event = entry.getEvent();
        CompetitionResultEntity competition = event.getCompetition();
        events.add(new TimeChangedEvent(eventType, mapper.entityToDto(competition), mapper.entityToDto(event), mapper.entityToDto(entry)));
    }

    @Override
    public void deleted(Collection<EntryResultEntity> entities) {
        for (EntryResultEntity entry : entities) {
            addEventForEntry(EventType.Deleted, entry);
        }
    }

    public void notifyListeners() {
        events.forEach(event -> listeners.forEach(l -> l.changed(event)));
    }

    public void setListeners(TimeChangeListener[] listeners) {
        this.listeners = Arrays.asList(listeners);
    }
}
