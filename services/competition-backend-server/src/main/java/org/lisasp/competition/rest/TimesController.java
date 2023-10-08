package org.lisasp.competition.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lisasp.competition.base.api.type.EventType;
import org.lisasp.competition.base.api.type.Gender;
import org.lisasp.competition.times.api.TimeDto;
import org.lisasp.competition.times.api.TimesQuery;
import org.lisasp.competition.times.service.TimesService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("time")
@RequiredArgsConstructor
public class TimesController {

    private final TimesService timesService;

    @ApiResponses(@ApiResponse(responseCode = "200", useReturnTypeSchema = true))
    @GetMapping("")
    @Transactional
    public TimeDto[] findTimes(@RequestParam("eventType") String eventType, @RequestParam("agegroup") String agegroup, @RequestParam("gender") String gender, @RequestParam("discipline") String discipline) {
        return timesService.findTimes(TimesQuery.builder().eventType(EventType.fromString(eventType)).agegroup(agegroup).gender(Gender.fromString(gender)).discipline(discipline).build());
    }
}
