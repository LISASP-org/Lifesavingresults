package org.lisasp.results.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lisasp.results.competition.api.CompetitionCreated;
import org.lisasp.results.competition.api.CompetitionDto;
import org.lisasp.results.competition.api.CreateCompetition;
import org.lisasp.results.competition.api.exception.NotFoundException;
import org.lisasp.results.competition.service.CompetitionService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class CompetitionController {

    private final CompetitionService service;

    @PostMapping("/competition")
    public CompetitionCreated createCompetition(@RequestBody CreateCompetition createCompetition) {
        log.info("Import from JAuswertung for {}", createCompetition.name());
        return service.execute(createCompetition);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Competition not found"),
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/competition/{id}")
    public CompetitionDto getCompetitionById(@PathVariable String id) throws NotFoundException {
        return service.findCompetition(id);
    }

    @GetMapping("/competition")
    public CompetitionDto[] findCompetitions() {
        return service.findCompetitions();
    }
}

