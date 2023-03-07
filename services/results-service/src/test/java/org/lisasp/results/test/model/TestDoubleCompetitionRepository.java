package org.lisasp.results.test.model;

import lombok.Setter;
import org.lisasp.results.service.CompetitionEntity;
import org.lisasp.results.service.CompetitionRepository;

import java.util.ArrayList;
import java.util.Optional;

public class TestDoubleCompetitionRepository extends TestDoubleCrudRepository<CompetitionEntity> implements CompetitionRepository {

    @Setter
    private TestDoubleEventRepository eventRepository;

    @Override
    public Optional<CompetitionEntity> findByUploadId(String uploadId) {
        return entries.values().stream().filter(e -> e.getUploadId().equals(uploadId)).findFirst();
    }

    @Override
    public <S extends CompetitionEntity> S save(S entity) {
        if (entity.getEvents() == null) {
            entity.setEvents(new ArrayList<>());
        }
        entity.getEvents().forEach(e -> e.setCompetition(entity));
        eventRepository.check(entity.getEvents());
        return super.save(entity);
    }

    public void check(CompetitionEntity competition) {
        entries.put(competition.getId(), competition);
    }
}
