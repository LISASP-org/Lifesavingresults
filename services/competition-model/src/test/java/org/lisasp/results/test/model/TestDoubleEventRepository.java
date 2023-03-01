package org.lisasp.results.test.model;

import lombok.Setter;
import org.lisasp.results.model.CompetitionEntity;
import org.lisasp.results.model.EventEntity;
import org.lisasp.results.model.EventRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestDoubleEventRepository extends TestDoubleCrudRepository<EventEntity> implements EventRepository {

    @Setter
    private TestDoubleCompetitionRepository competitionRepository;

    @Setter
    private TestDoubleEntryRepository entryRepository;

    @Override
    public List<EventEntity> findAllByCompetitionId(String competitionId) {
        return entries.values().stream().filter(e -> e.getCompetition().getId().equals(competitionId)).toList();
    }

    @Override
    public boolean existsByCompetitionIdAndId(String competitionId, String eventId) {
        return entries.values().stream().anyMatch(e -> e.getId().equals(eventId) && e.getCompetition().getId().equals(competitionId));
    }

    public void check(List<EventEntity> events) {
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
    public <S extends EventEntity> S save(S entity) {
        CompetitionEntity competition = entity.getCompetition();
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
