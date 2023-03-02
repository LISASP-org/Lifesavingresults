package org.lisasp.results;

import lombok.RequiredArgsConstructor;
import org.lisasp.results.imports.core.ImportService;
import org.lisasp.results.imports.core.ImportStorage;
import org.lisasp.results.model.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@SpringBootApplication
public class ResultsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResultsApplication.class, args);
	}

	@Bean
	CompetitionService competitionService(CompetitionRepository competitionRepository, EventRepository eventRepository, EntryRepository entryRepository) {
		return new CompetitionService(competitionRepository, eventRepository, entryRepository);
	}

	@Bean
	EventService eventService(CompetitionRepository competitionRepository, EventRepository eventRepository, EntryRepository entryRepository) {
		return new EventService(competitionRepository, eventRepository);
	}

	@Bean
	EntryService entryService(CompetitionRepository competitionRepository, EventRepository eventRepository, EntryRepository entryRepository) {
		return new EntryService(eventRepository, entryRepository);
	}

	@Bean
	ImportStorage importStorage(ConfigurationValues config) {
		return new ImportStorage(config);
	}

	@Bean
	ImportService importService(CompetitionService competitionService, ImportStorage importStorage) {
		return new ImportService(competitionService, importStorage);
	}
}
