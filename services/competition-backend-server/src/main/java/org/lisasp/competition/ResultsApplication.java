package org.lisasp.competition;

import org.lisasp.competition.authorization.service.AuthorizationService;
import org.lisasp.competition.authorization.service.RoleService;
import org.lisasp.competition.authorization.service.users.UserRoleRepository;
import org.lisasp.competition.authorization.service.users.UserRoleService;
import org.lisasp.competition.base.api.exception.CouldNotInitializeException;
import org.lisasp.competition.results.service.*;
import org.lisasp.competition.results.service.imports.ImportService;
import org.lisasp.competition.results.service.imports.ImportStorage;
import org.lisasp.competition.service.CompetitionRepository;
import org.lisasp.competition.service.CompetitionService;
import org.lisasp.competition.times.service.TimesRepository;
import org.lisasp.competition.times.service.TimesService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ResultsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResultsApplication.class, args);
    }

    @Bean
    CompetitionService competitionService(CompetitionRepository competitionRepository, ResultService resultService) {
        return new CompetitionService(competitionRepository).register(new CompetitionChangeAdapter(resultService));
    }

    @Bean
    ResultService competitionResultService(CompetitionResultRepository competitionResultRepository,
                                           EventResultRepository eventResultRepository,
                                           EntryResultRepository entryResultRepository,
                                           TimesService timesService) {
        return new ResultService(competitionResultRepository, eventResultRepository, entryResultRepository).register(timesService::receive);
    }

    @Bean
    ImportStorage importStorage(ConfigurationValues config) {
        return new ImportStorage(config);
    }

    @Bean
    ImportService importService(ResultService resultService, ImportStorage importStorage) {
        return new ImportService(resultService, importStorage);
    }

    @Bean
    TimesService timesService(TimesRepository repository) {
        return new TimesService(repository);
    }

    @Bean
    RoleService roleService() throws CouldNotInitializeException {
        return new RoleService().loadRoles(getClass().getResourceAsStream("/roles.yaml"));
    }

    @Bean
    UserRoleService userRoleService(UserRoleRepository userRoleRepository) {
        return new UserRoleService(userRoleRepository);
    }

    @Bean
    AuthorizationService authorizationService(RoleService roleService, UserRoleService userRoleService) {
        return new AuthorizationService(roleService, userRoleService);
    }
}
