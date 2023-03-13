package org.lisasp.competition.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.lisasp.competition.results.api.EntryDto;
import org.lisasp.competition.results.api.exception.NotFoundException;
import org.lisasp.competition.results.service.EntryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class EntryController {

    private final EntryService service;

    @GetMapping("/competition/{competitionId}/event/{eventId}/entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Competition not found or event not found in competition"),
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @Transactional
    public EntryDto[] findEntriesByEvent(@PathVariable String competitionId, @PathVariable String eventId) throws NotFoundException {
        return service.findEntries(competitionId, eventId);
    }
}

