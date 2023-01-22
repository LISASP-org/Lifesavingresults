package org.lisasp.results;

import lombok.RequiredArgsConstructor;
import org.lisasp.results.api.Competition;
import org.lisasp.results.api.dto.CompetitionCreated;
import org.lisasp.results.api.dto.CompetitionDto;
import org.lisasp.results.api.dto.CreateCompetition;
import org.lisasp.results.api.dto.TimeDto;
import org.lisasp.results.api.type.EventTypes;
import org.lisasp.results.api.type.Genders;
import org.lisasp.results.model.competition.CompetitionService;
import org.lisasp.results.model.exception.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class CompetitionRestService {

    private final CompetitionService service;

    @PostMapping("/competition")
    public CompetitionCreated create(@RequestBody CreateCompetition createCompetition) {
        return service.execute(createCompetition);
    }

    @GetMapping("/competition/{id}")
    public CompetitionDto get(@PathVariable String id) throws NotFoundException {
        return service.findCompetition(id);
    }

    @GetMapping("/competition")
    public CompetitionDto[] findAll() {
        return service.findCompetitions();
    }

    @GetMapping("/competition/{id}/time")
    public TimeDto[] findAll(@PathVariable String id) {
        return switch (id) {
            case "1" -> new TimeDto[]{
                    new TimeDto("10", 0, EventTypes.INDIVIDUAL, "Jana Schmidt", "OG Beispiel", "GER", "AK 12", Genders.FEMALE, "50m Hindernisschwimmen", 543200, "S1"),
                    new TimeDto("11", 0, EventTypes.INDIVIDUAL, "Hans Kurz", "OG Ausnahme", "GER", "AK 12", Genders.MALE, "50m Flossenschwimmen", 432100, "S1"),
            };
            case "2" -> new TimeDto[]{
                    new TimeDto("20", 0, EventTypes.TEAM, "OG Vorne", "OG Vorne", "GER", "AK 13/14", Genders.MALE, "4x50m Hindernisschwimmen", 2010900, ""),
                    new TimeDto("21", 0, EventTypes.TEAM, "OG Hinten", "OG Hinten", "GER", "AK 13/14", Genders.MALE, "4x50m Hindernisschwimmen", 2010900, ""),
            };
            default -> new TimeDto[0];
        };
    }
}

