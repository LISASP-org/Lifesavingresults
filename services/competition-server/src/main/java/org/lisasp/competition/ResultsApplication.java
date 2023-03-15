package org.lisasp.competition;

import lombok.RequiredArgsConstructor;
import org.lisasp.competition.results.service.*;
import org.lisasp.competition.results.service.imports.ImportService;
import org.lisasp.competition.results.service.imports.ImportStorage;
import org.lisasp.competition.service.CompetitionRepository;
import org.lisasp.competition.service.CompetitionService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
@SpringBootApplication
public class ResultsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResultsApplication.class, args);
    }

    @Bean
    CompetitionService competitionService(CompetitionRepository competitionRepository) {
        return new CompetitionService(competitionRepository);
    }

    @Bean
    CompetitionResultService competitionResultService(CompetitionResultRepository competitionResultRepository,
                                                      EventRepository eventRepository,
                                                      EntryRepository entryRepository) {
        return new CompetitionResultService(competitionResultRepository, eventRepository, entryRepository);
    }

    @Bean
    EventResultService eventResultService(CompetitionResultRepository competitionResultRepository,
                                          EventRepository eventRepository,
                                          EntryRepository entryRepository) {
        return new EventResultService(competitionResultRepository, eventRepository);
    }

    @Bean
    EntryResultService entryResultService(CompetitionResultRepository competitionResultRepository,
                                          EventRepository eventRepository,
                                          EntryRepository entryRepository) {
        return new EntryResultService(eventRepository, entryRepository);
    }

    @Bean
    ImportStorage importStorage(ConfigurationValues config) {
        return new ImportStorage(config);
    }

    @Bean
    ImportService importService(CompetitionResultService competitionResultService, ImportStorage importStorage) {
        return new ImportService(competitionResultService, importStorage);
    }
}
