package org.lisasp.results;

import lombok.RequiredArgsConstructor;
import org.lisasp.competition.api.dto.EventDto;
import org.lisasp.results.model.EventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class EventRestService {

    private final EventService service;


    @GetMapping("/competition/{competitionId}/event")
    public EventDto[] findAll(@PathVariable String competitionId) {
        return service.findEvents(competitionId);
    }
}

