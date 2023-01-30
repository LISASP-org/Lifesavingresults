package org.lisasp.results;

import lombok.RequiredArgsConstructor;
import org.lisasp.competition.api.dto.EntryDto;
import org.lisasp.competition.api.dto.EventDto;
import org.lisasp.results.model.EntryService;
import org.lisasp.results.model.EventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class EntryRestService {

    private final EntryService service;


    @GetMapping("/event/{eventId}/entry")
    @Operation(operationId = "findAllEntriesByEventId")
    public EntryDto[] findAll(@PathVariable String eventId) {
        return service.findEntries(eventId);
    }
}

