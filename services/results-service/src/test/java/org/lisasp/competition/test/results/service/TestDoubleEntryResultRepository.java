package org.lisasp.competition.test.results.service;

import lombok.Setter;
import org.lisasp.competition.results.service.EntryResultEntity;
import org.lisasp.competition.results.service.EntryResultRepository;
import org.lisasp.competition.results.service.EventResultEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Setter
public class TestDoubleEntryResultRepository extends TestDoubleCrudRepository<EntryResultEntity> implements EntryResultRepository {

    private TestDoubleEventResultRepository eventRepository;

    @Override
    public List<EntryResultEntity> findAllByEventId(String eventId) {
        return entries.values().stream().filter(e -> e.getEvent().getId().equals(eventId)).toList();
    }

    @Override
    public List<String> findAllEntryIdByEventId(Collection<String> eventIds) {
        return entries.values().stream().filter(e -> eventIds.contains(e.getEvent().getId())).map(e -> e.getId()).toList();
    }

    public void check(List<EntryResultEntity> entities) {
        entities.forEach(e -> entries.put(e.getId(), e));
    }

    @Override
    public <S extends EntryResultEntity> S save(S entity) {
        EventResultEntity event = entity.getEvent();
        if (event.getEntries() == null) {
            event.setEntries(new ArrayList<>());
        }
        if (event.getEntries().stream().noneMatch(e -> e.getId().equals(entity.getId()))) {
            event.getEntries().add(entity);
        }
        eventRepository.check(List.of(event));
        return super.save(entity);
    }
}
