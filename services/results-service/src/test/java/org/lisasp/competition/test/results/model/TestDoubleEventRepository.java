package org.lisasp.competition.test.results.model;

import lombok.Setter;
import org.lisasp.competition.results.service.CompetitionResultEntity;
import org.lisasp.competition.results.service.EventResultEntity;
import org.lisasp.competition.results.service.EventRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestDoubleEventRepository extends TestDoubleCrudRepository<EventResultEntity> implements EventRepository {

    @Setter
    private TestDoubleCompetitionResultRepository competitionRepository;

    @Setter
    private TestDoubleEntryRepository entryRepository;

    @Override
    public List<EventResultEntity> findAllByCompetitionId(String competitionId) {
        return entries.values().stream().filter(e -> e.getCompetition().getId().equals(competitionId)).toList();
    }

    @Override
    public boolean existsByCompetitionIdAndId(String competitionId, String eventId) {
        return entries.values().stream().anyMatch(e -> e.getId().equals(eventId) && e.getCompetition().getId().equals(competitionId));
    }

    public void check(List<EventResultEntity> events) {
        events.forEach(e -> {
            entries.put(e.getId(), e);
            if (e.getEntries() == null) {
                e.setEntries(new ArrayList<>());
            }
            e.getEntries().forEach(entry -> entry.setEvent(e));
            entryRepository.check(e.getEntries());
        });
    }

    @Override
    public <S extends EventResultEntity> S save(S entity) {
        CompetitionResultEntity competition = entity.getCompetition();
        if (competition.getEvents() == null) {
            competition.setEvents(new ArrayList<>());
        }
        if (competition.getEvents().stream().noneMatch(e -> e.getId().equals(entity.getId()))) {
            competition.getEvents().add(entity);
        }
        competitionRepository.check(competition);
        check(Arrays.asList(entity));
        return super.save(entity);
    }
}
