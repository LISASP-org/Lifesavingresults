package org.lisasp.results.test.competition.service;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"org.lisasp.results.competition.model"})
@ComponentScan(basePackages = {"org.lisasp.results.competition.service", "org.lisasp.results.test"})
@EnableJpaRepositories(basePackages = {"org.lisasp.results.competition.model"})
public class TestApplication {
}
