package org.lisasp.results;

import lombok.RequiredArgsConstructor;
import org.lisasp.results.service.CompetitionRepository;
import org.lisasp.results.service.EntryRepository;
import org.lisasp.results.service.EventRepository;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor
@Component
class DatabaseCleaner {

    private final CompetitionRepository competitionRepository;
    private final EventRepository eventRepository;
    private final EntryRepository entryRepository;
    public void clean() {
        entryRepository.deleteAll();
        eventRepository.deleteAll();
        competitionRepository.deleteAll();
        // TestTransaction.flagForCommit();
        // TestTransaction.end();

        assertEquals(0, competitionRepository.count());
        assertEquals(0, eventRepository.count());
        assertEquals(0, entryRepository.count());
    }
}
