package org.lisasp.results.model.competition;

import org.lisasp.results.model.competition.CompetitionEntity;
import org.springframework.data.repository.CrudRepository;

public interface CompetitionRepository extends CrudRepository<CompetitionEntity, String> {
}
