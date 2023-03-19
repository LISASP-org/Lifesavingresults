package org.lisasp.competition.results.service;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CompetitionResultRepository extends CrudRepository<CompetitionResultEntity, String> {
    Optional<CompetitionResultEntity> findByUploadId(String uploadId);
}
