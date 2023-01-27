package org.lisasp.results.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventRepository extends CrudRepository<EventEntity, String> {
    List<EventEntity> findAllByCompetitionId(String competitionId);
}
