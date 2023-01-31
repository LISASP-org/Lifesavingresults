package org.lisasp.results.competition.model;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CompetitionRepository extends CrudRepository<CompetitionEntity, String> {
    public Optional<CompetitionEntity> findByUploadId(String uploadId);
}
