package org.lisasp.competition.authorization.service;

import lombok.RequiredArgsConstructor;
import org.lisasp.competition.authorization.service.users.UserRoleRepository;
import org.lisasp.competition.authorization.service.users.UserRoleService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
@SpringBootApplication
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    @Bean
    UserRoleService userRoleService(UserRoleRepository repository) {
        return new UserRoleService(repository);
    }

    @Bean
    TestDatabaseHelper databaseCleaner(UserRoleRepository repository) {
        return new TestDatabaseHelper(repository);
    }

}
