package org.lisasp.results.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.lisasp.results.api.EventDto;
import org.lisasp.results.api.exception.NotFoundException;
import org.lisasp.results.service.EventService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class EventController {

    private final EventService service;


    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Competition not found"),
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/competition/{competitionId}/event")
    @Transactional
    public EventDto[] findEventsByCompetitionId(@PathVariable String competitionId) throws NotFoundException {
        return service.findEvents(competitionId);
    }
}

