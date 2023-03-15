package org.lisasp.competition.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lisasp.competition.api.CompetitionCreated;
import org.lisasp.competition.api.CompetitionDto;
import org.lisasp.competition.api.CreateCompetition;
import org.lisasp.competition.base.api.exception.NotFoundException;
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

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @PostMapping("")
    @Transactional
    public CompetitionCreated createCompetition(@RequestBody CreateCompetition createCompetition) {
        log.info("Import from JAuswertung for {}", createCompetition.name());
        return competitionService.execute(createCompetition);
    }

    @ApiResponses(
            value = {@ApiResponse(responseCode = "404", description = "Competition not found"), @ApiResponse(responseCode = "200", useReturnTypeSchema = true)})
    @GetMapping("/{id}")
    @Transactional
    public CompetitionDto getCompetitionById(@PathVariable String id) throws NotFoundException {
        return competitionService.findCompetition(id);
    }

    @GetMapping("")
    @Transactional
    public CompetitionDto[] findCompetitions() {
        return competitionService.findCompetitions();
    }
}
