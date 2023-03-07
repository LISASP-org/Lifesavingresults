package org.lisasp.results.test.model;

import lombok.Setter;
import org.lisasp.results.service.EntryEntity;
import org.lisasp.results.service.EntryRepository;
import org.lisasp.results.service.EventEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestDoubleEntryRepository extends TestDoubleCrudRepository<EntryEntity> implements EntryRepository {

    @Setter
    private TestDoubleEventRepository eventRepository;
    @Override
    public List<EntryEntity> findAllByEventId(String eventId) {
        return entries.values().stream().filter(e -> e.getEvent().getId().equals(eventId)).toList();
    }

    public void check(List<EntryEntity> entities) {
        entities.forEach(e -> entries.put(e.getId(), e));
    }

    @Override
    public <S extends EntryEntity> S save(S entity) {
        EventEntity event = entity.getEvent();
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
