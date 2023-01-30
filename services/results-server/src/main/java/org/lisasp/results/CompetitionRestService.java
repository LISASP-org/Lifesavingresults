package org.lisasp.results;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lisasp.competition.api.dto.CompetitionCreated;
import org.lisasp.competition.api.dto.CompetitionDto;
import org.lisasp.competition.api.dto.CreateCompetition;
import org.lisasp.competition.api.dto.TimeDto;
import org.lisasp.results.base.api.type.EventType;
import org.lisasp.results.base.api.type.Gender;
import org.lisasp.results.model.CompetitionService;
import org.lisasp.competition.api.exception.NotFoundException;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;

@Slf4j
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class CompetitionRestService {

    private final CompetitionService service;

    @PostMapping("/competition")
    @Operation(operationId = "createCompetition")
    public CompetitionCreated create(@RequestBody CreateCompetition createCompetition) {
        log.info("Import from JAuswertung for {}", createCompetition.name());
        return service.execute(createCompetition);
    }

    @GetMapping("/competition/{id}")
    @Operation(operationId = "getCompetitionById")
    public CompetitionDto get(@PathVariable String id) throws NotFoundException {
        return service.findCompetition(id);
    }

    @GetMapping("/competition")
    @Operation(operationId = "findAllCompetitions")
    public CompetitionDto[] findAll() {
        return service.findCompetitions();
    }

    @GetMapping("/competition/{id}/time")
    @Operation(operationId = "findAllTimesByCompetitionId")
    public TimeDto[] findAll(@PathVariable String id) {
        return switch (id) {
            case "1" -> new TimeDto[]{
                    new TimeDto("10", 0, EventType.Individual, "Jana Schmidt", "OG Beispiel", "GER", "AK 12", Gender.Female, "50m Hindernisschwimmen", 543200, "S1"),
                    new TimeDto("11", 0, EventType.Individual, "Hans Kurz", "OG Ausnahme", "GER", "AK 12", Gender.Male, "50m Flossenschwimmen", 432100, "S1"),
            };
            case "2" -> new TimeDto[]{
                    new TimeDto("20", 0, EventType.Team, "OG Vorne", "OG Vorne", "GER", "AK 13/14", Gender.Male, "4x50m Hindernisschwimmen", 2010900, ""),
                    new TimeDto("21", 0, EventType.Team, "OG Hinten", "OG Hinten", "GER", "AK 13/14", Gender.Male, "4x50m Hindernisschwimmen", 2010900, ""),
            };
            default -> new TimeDto[0];
        };
    }
}

