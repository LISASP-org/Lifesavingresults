package org.lisasp.competition.test.results.model;

import lombok.Setter;
import org.lisasp.competition.results.service.EntryResultEntity;
import org.lisasp.competition.results.service.EntryRepository;
import org.lisasp.competition.results.service.EventResultEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestDoubleEntryRepository extends TestDoubleCrudRepository<EntryResultEntity> implements EntryRepository {

    @Setter
    private TestDoubleEventRepository eventRepository;
    @Override
    public List<EntryResultEntity> findAllByEventId(String eventId) {
        return entries.values().stream().filter(e -> e.getEvent().getId().equals(eventId)).toList();
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
        eventRepository.check(Arrays.asList(event));
        return super.save(entity);
    }
}
