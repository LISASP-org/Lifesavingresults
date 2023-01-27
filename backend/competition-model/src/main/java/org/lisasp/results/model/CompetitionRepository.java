package org.lisasp.results.model;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CompetitionRepository extends CrudRepository<CompetitionEntity, String> {
    Optional<CompetitionEntity> findByUploadId(String uploadId);
}
