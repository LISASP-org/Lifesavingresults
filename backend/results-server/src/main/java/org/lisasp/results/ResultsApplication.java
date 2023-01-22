package org.lisasp.results;

import lombok.RequiredArgsConstructor;
import org.lisasp.results.model.competition.CompetitionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@SpringBootApplication
public class ResultsApplication implements CommandLineRunner {

    private final CompetitionService competitionService;

	public static void main(String[] args) {
		SpringApplication.run(ResultsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
