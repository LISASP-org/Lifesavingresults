package org.lisasp.results.test.competition.service;

import lombok.RequiredArgsConstructor;
import org.lisasp.results.competition.model.CompetitionRepository;
import org.lisasp.results.competition.model.EntryRepository;
import org.lisasp.results.competition.model.EventRepository;
import org.springframework.stereotype.Component;

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
    }
}
