package org.lisasp.competition.results.service;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface EntryResultRepository extends CrudRepository<EntryResultEntity, String> {
    List<EntryResultEntity> findAllByEventId(String eventId);

    @Query("select e from EntryResultEntity e where e.event.id in :ids")
    List<String> findAllEntryIdByEventId(@Param("ids") Collection<String> eventIds);
}
