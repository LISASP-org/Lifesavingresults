package org.lisasp.results.test.model;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

@SpringBootApplication
@EntityScan(basePackages = {"org.lisasp.results.model"})
@ComponentScan(basePackages = {"org.lisasp.results.model", "org.lisasp.results.test"})
@EnableJpaRepositories(basePackages= {"org.lisasp.results.model"})
public class TestApplication {
}
