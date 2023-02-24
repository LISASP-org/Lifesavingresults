package org.lisasp.results.model;

import org.lisasp.results.model.EntryEntity;
import org.lisasp.results.model.EventEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface EntryRepository extends CrudRepository<EntryEntity, String> {
    List<EntryEntity> findAllByEventId(String eventId);
}
