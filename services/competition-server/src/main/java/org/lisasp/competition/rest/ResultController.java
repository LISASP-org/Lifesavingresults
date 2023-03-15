package org.lisasp.competition.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lisasp.competition.base.api.exception.FileFormatException;
import org.lisasp.competition.base.api.exception.NotFoundException;
import org.lisasp.competition.results.api.*;
import org.lisasp.competition.results.service.CompetitionResultService;
import org.lisasp.competition.results.service.EntryResultService;
import org.lisasp.competition.results.service.EventResultService;
import org.lisasp.competition.results.service.imports.ImportService;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static org.lisasp.competition.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@Slf4j
@RestController
@RequestMapping("result")
@RequiredArgsConstructor
public class ResultController {

    private final CompetitionResultService competitionResultService;
    private final EventResultService eventResultService;
    private final EntryResultService entryResultService;
    private final ImportService importService;


    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @PostMapping("/competition")
    @Transactional
    public CompetitionCreated createCompetition(@RequestBody CreateCompetition createCompetition) {
        log.info("Import from JAuswertung for {}", createCompetition.name());
        return competitionResultService.execute(createCompetition);
    }

    @ApiResponses(
            value = {@ApiResponse(responseCode = "404", description = "Competition not found"), @ApiResponse(responseCode = "200", useReturnTypeSchema = true)})
    @GetMapping("/competition/{id}")
    @Transactional
    public CompetitionDto getCompetitionById(@PathVariable String id) throws NotFoundException {
        return competitionResultService.findCompetition(id);
    }

    @GetMapping("/competition")
    @Transactional
    public CompetitionDto[] findCompetitions() {
        return competitionResultService.findCompetitions();
    }

    @ApiResponses(
            value = {@ApiResponse(responseCode = "404", description = "Competition not found"), @ApiResponse(responseCode = "200", useReturnTypeSchema = true)})
    @GetMapping("/competition/{competitionId}/event")
    @Transactional
    public EventDto[] findEventsByCompetitionId(@PathVariable String competitionId) throws NotFoundException {
        return eventResultService.findEvents(competitionId);
    }

    @GetMapping("/competition/{competitionId}/event/{eventId}/entry")
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "Competition not found or event not found in competition"),
                           @ApiResponse(responseCode = "200", useReturnTypeSchema = true)})
    @Transactional
    public EntryDto[] findEntriesByEvent(@PathVariable String competitionId, @PathVariable String eventId) throws NotFoundException {
        return entryResultService.findEntries(competitionId, eventId);
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "Competition not found"),
                           @ApiResponse(responseCode = "400", description = "The provided data could not be parsed"),
                           @ApiResponse(responseCode = "204", description = "Import successful", useReturnTypeSchema = true)})
    @PutMapping("/import/{uploadId}/jauswertung")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void importFromJAuswertung(@PathVariable String uploadId, @RequestBody String competition) throws NotFoundException, FileFormatException {
        importService.importFromJAuswertung(uploadId, competition);
    }
}
