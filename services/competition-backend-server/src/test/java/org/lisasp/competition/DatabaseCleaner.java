package org.lisasp.competition;

import lombok.RequiredArgsConstructor;
import org.lisasp.competition.results.service.CompetitionResultRepository;
import org.lisasp.competition.results.service.EntryResultRepository;
import org.lisasp.competition.results.service.EventResultRepository;
import org.lisasp.competition.service.CompetitionRepository;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor
@Component
class DatabaseCleaner {

    private final CompetitionRepository competitionRepository;
    private final CompetitionResultRepository competitionResultRepository;
    private final EventResultRepository eventResultRepository;
    private final EntryResultRepository entryResultRepository;

    public void clean() {
        competitionRepository.deleteAll();
        entryResultRepository.deleteAll();
        eventResultRepository.deleteAll();
        competitionResultRepository.deleteAll();

        assertEquals(0, competitionRepository.count());
        assertEquals(0, competitionResultRepository.count());
        assertEquals(0, eventResultRepository.count());
        assertEquals(0, entryResultRepository.count());
    }
}
