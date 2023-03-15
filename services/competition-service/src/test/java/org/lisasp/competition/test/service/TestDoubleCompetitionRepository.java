package org.lisasp.competition.test.service;

import org.lisasp.competition.service.CompetitionEntity;
import org.lisasp.competition.service.CompetitionRepository;

import java.util.Optional;

public class TestDoubleCompetitionRepository extends TestDoubleCrudRepository<CompetitionEntity> implements CompetitionRepository {

    @Override
    public Optional<CompetitionEntity> findByUploadId(String uploadId) {
        return entries.values().stream().filter(e -> e.getUploadId().equals(uploadId)).findFirst();
    }

    @Override
    public <S extends CompetitionEntity> S save(S entity) {
        return super.save(entity);
    }

    public void check(CompetitionEntity competition) {
        entries.put(competition.getId(), competition);
    }
}
