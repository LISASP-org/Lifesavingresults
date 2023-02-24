package org.lisasp.results.test.imports.core;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"org.lisasp.results.model"})
@ComponentScan(basePackages = {"org.lisasp.results.model", "org.lisasp.results.imports.core", "org.lisasp.results.test"})
@EnableJpaRepositories(basePackages= {"org.lisasp.results.model"})
public class TestApplication {
}
