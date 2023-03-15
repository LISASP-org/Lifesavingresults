package org.lisasp.competition.test.results.model;

import lombok.Setter;
import org.lisasp.competition.results.service.CompetitionResultEntity;
import org.lisasp.competition.results.service.CompetitionResultRepository;

import java.util.ArrayList;
import java.util.Optional;

public class TestDoubleCompetitionResultRepository extends TestDoubleCrudRepository<CompetitionResultEntity> implements CompetitionResultRepository {

    @Setter
    private TestDoubleEventRepository eventRepository;

    @Override
    public Optional<CompetitionResultEntity> findByUploadId(String uploadId) {
        return entries.values().stream().filter(e -> e.getUploadId().equals(uploadId)).findFirst();
    }

    @Override
    public <S extends CompetitionResultEntity> S save(S entity) {
        if (entity.getEvents() == null) {
            entity.setEvents(new ArrayList<>());
        }
        entity.getEvents().forEach(e -> e.setCompetition(entity));
        eventRepository.check(entity.getEvents());
        return super.save(entity);
    }

    public void check(CompetitionResultEntity competition) {
        entries.put(competition.getId(), competition);
    }
}
