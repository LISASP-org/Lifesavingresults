package org.lisasp.competition.results.service;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventRepository extends CrudRepository<EventEntity, String> {
    List<EventEntity> findAllByCompetitionId(String competitionId);

    boolean existsByCompetitionIdAndId(String competitionId, String eventId);
}
