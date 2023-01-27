package org.lisasp.results;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@SpringBootApplication
public class ResultsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResultsApplication.class, args);
	}
}
