package org.lisasp.competition.results.service;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EntryRepository extends CrudRepository<EntryEntity, String> {
    List<EntryEntity> findAllByEventId(String eventId);
}
