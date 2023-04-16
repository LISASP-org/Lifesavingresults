package org.lisasp.competition.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lisasp.competition.api.CompetitionDto;
import org.lisasp.competition.api.CreateCompetition;
import org.lisasp.competition.base.api.exception.NotFoundException;
import org.lisasp.competition.security.Rights;
import org.lisasp.competition.security.CompetitionServerAuthorizationService;
import org.lisasp.competition.security.Roles;
import org.lisasp.competition.service.CompetitionService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static org.lisasp.competition.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@Slf4j
@RestController
@RequestMapping("competition")
@RequiredArgsConstructor
public class CompetitionController {

    private final CompetitionService competitionService;

    private final CompetitionServerAuthorizationService authorizationService;

    @ApiResponses({@ApiResponse(responseCode = "403", description = "user is not authorized"), @ApiResponse(responseCode = "200", useReturnTypeSchema = true)})
    @Operation(security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME))
    @PostMapping("")
    @Transactional
    public CompetitionDto createCompetition(@RequestBody CreateCompetition createCompetition) {
        log.debug("Creating competition: {}", createCompetition.getName());
        CompetitionDto competition = competitionService.create(createCompetition);
        setCurrentUserAsOrganizer(competition);
        return competition;
    }

    @ApiResponses({@ApiResponse(responseCode = "404", description = "Competition not found"), @ApiResponse(responseCode = "403", description = "user is not authorized"), @ApiResponse(responseCode = "200", useReturnTypeSchema = true)})
    @Operation(security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME))
    @PutMapping("")
    @Transactional
    public CompetitionDto updateCompetition(@RequestBody CompetitionDto competition) throws NotFoundException {
        log.debug("Updating Competition: {}", competition.name());
        assertAuthorized(Rights.CompetitionEdit, competition.id());
        return competitionService.update(competition);
    }

    @ApiResponses({@ApiResponse(responseCode = "404", description = "Competition not found"), @ApiResponse(responseCode = "200", useReturnTypeSchema = true)})
    @GetMapping("/{id}")
    @Transactional
    public CompetitionDto getCompetitionById(@PathVariable String id) throws NotFoundException {
        return competitionService.findCompetition(id);
    }

    @ApiResponses({@ApiResponse(responseCode = "403", description = "user is not authorized"), @ApiResponse(responseCode = "204", useReturnTypeSchema = true)})
    @DeleteMapping("/{id}")
    @Transactional
    public void deleteCompetitionById(@PathVariable String id) {
        assertAuthorized(Rights.CompetitionDelete, id);
        competitionService.deleteCompetition(id);
    }

    @ApiResponses(@ApiResponse(responseCode = "200", useReturnTypeSchema = true))
    @GetMapping("")
    @Transactional
    public CompetitionDto[] findCompetitions() {
        return competitionService.findCompetitions();
    }

    private void setCurrentUserAsOrganizer(CompetitionDto competition) {
        authorizationService.addUserRole(Roles.CompetitionOrganizer, competition.id());
    }

    private void assertAuthorized(Rights right, String id) {
        authorizationService.assertAuthorization(right, id);
    }
}
