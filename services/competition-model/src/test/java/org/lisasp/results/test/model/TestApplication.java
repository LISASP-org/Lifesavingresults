package org.lisasp.results.test.model;

import org.lisasp.results.model.CompetitionEntity;
import org.lisasp.results.model.CompetitionRepository;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EntityScan(basePackageClasses = CompetitionEntity.class)
@ComponentScan(basePackages = {"org.lisasp.results.model", "org.lisasp.results.test"})
@EnableJpaRepositories(basePackageClasses = CompetitionRepository.class)
public class TestApplication {
}
