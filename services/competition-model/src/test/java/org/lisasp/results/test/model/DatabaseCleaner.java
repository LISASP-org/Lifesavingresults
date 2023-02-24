package org.lisasp.results.test.model;

import lombok.RequiredArgsConstructor;
import org.lisasp.results.model.CompetitionRepository;
import org.lisasp.results.model.EntryRepository;
import org.lisasp.results.model.EventRepository;
import org.springframework.stereotype.Component;
import org.springframework.test.context.transaction.TestTransaction;

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
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }
}
