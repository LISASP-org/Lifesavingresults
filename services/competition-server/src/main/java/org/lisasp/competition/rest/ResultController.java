package org.lisasp.competition.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lisasp.competition.base.api.exception.FileFormatException;
import org.lisasp.competition.base.api.exception.InvalidDataException;
import org.lisasp.competition.base.api.exception.NotFoundException;
import org.lisasp.competition.results.api.CompetitionDto;
import org.lisasp.competition.results.api.EntryDto;
import org.lisasp.competition.results.api.EventDto;
import org.lisasp.competition.results.service.ResultService;
import org.lisasp.competition.results.service.imports.ImportService;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("result")
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;
    private final ImportService importService;

    @ApiResponses({@ApiResponse(responseCode = "404", description = "Competition not found"), @ApiResponse(responseCode = "200", useReturnTypeSchema = true)})
    @GetMapping("/competition/{id}")
    @Transactional
    public CompetitionDto getCompetitionById(@PathVariable String id) throws NotFoundException {
        return resultService.findCompetition(id);
    }

    @GetMapping("/competition")
    @Transactional
    public CompetitionDto[] findCompetitions() {
        return resultService.findCompetitions();
    }

    @ApiResponses({@ApiResponse(responseCode = "404", description = "Competition not found"), @ApiResponse(responseCode = "200", useReturnTypeSchema = true)})
    @GetMapping("/competition/{competitionId}/event")
    @Transactional
    public EventDto[] findEventsByCompetitionId(@PathVariable String competitionId) throws NotFoundException {
        return resultService.findEvents(competitionId);
    }

    @GetMapping("/competition/{competitionId}/event/{eventId}/entry")
    @ApiResponses({@ApiResponse(responseCode = "404", description = "Competition not found or event not found in competition"),
                   @ApiResponse(responseCode = "200", useReturnTypeSchema = true)})
    @Transactional
    public EntryDto[] findEntriesByEvent(@PathVariable String competitionId, @PathVariable String eventId) throws NotFoundException {
        return resultService.findEntries(competitionId, eventId);
    }

    @ApiResponses({@ApiResponse(responseCode = "404", description = "Competition not found"),
                   @ApiResponse(responseCode = "400", description = "The provided data could not be parsed"),
                   @ApiResponse(responseCode = "204", description = "Import successful", useReturnTypeSchema = true)})
    @PutMapping("/import/jauswertung/{uploadId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void importFromJAuswertung(@PathVariable String uploadId, @RequestBody String competition) throws NotFoundException, FileFormatException,
                                                                                                             InvalidDataException {
        importService.importFromJAuswertung(uploadId, competition);
    }
}
