package org.lisasp.competition.results.service;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface EventResultRepository extends CrudRepository<EventResultEntity, String> {

    @Query("select e.id from EventResultEntity e where e.competition.id = ?1")
    List<String> findAllEventIdByCompetitionId(String competitionId);

    List<EventResultEntity> findAllByCompetitionId(String competitionId);

    boolean existsByCompetitionIdAndId(String competitionId, String eventId);
}
