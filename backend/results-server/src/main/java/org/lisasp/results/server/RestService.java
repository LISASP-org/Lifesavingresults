package org.lisasp.results.server;

import lombok.RequiredArgsConstructor;
import org.lisasp.results.competitions.Competition;
import org.lisasp.results.competitions.EventType;
import org.lisasp.results.competitions.Gender;
import org.lisasp.results.competitions.Time;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class RestService {

    @GetMapping("/competition")
    public Competition[] findAll() {
        return new Competition[]{
                new Competition("1", "Deutsche Einzelstreckenmeisterschaften 2022", "DEM2022", LocalDate.of(2022, Month.MARCH, 26), LocalDate.of(2022, Month.MARCH, 27)),
                new Competition("2", "Deutsche Mehrkampfmeisterschaften 2022", "DMM2022", LocalDate.of(2022, Month.OCTOBER, 20), LocalDate.of(2022, Month.OCTOBER, 23))
        };
    }

    @GetMapping("/competition/{id}/time")
    public Time[] findAll(@PathVariable String id) {
        switch (id) {
            case "1": return new Time[]{
                    new Time("10", EventType.INDIVIDUAL, "Jana Schmidt",  "OG Beispiel", "GER", "AK 12", Gender.FEMALE,"50m Hindernisschwimmen", 543200, "S1"),
                    new Time("11", EventType.INDIVIDUAL, "Hans Kurz",  "OG Ausnahme", "GER", "AK 12", Gender.MALE,"50m Flossenschwimmen", 432100, "S1"),
            };
            case "2": return new Time[]{
                    new Time("20", EventType.TEAM, "OG Beispiel",  "OG Beispiel", "GER", "AK 13/14", Gender.MALE,"4x50m Hindernisschwimmen", 2010900, ""),
                    new Time("21", EventType.TEAM, "OG Ausnahme",  "OG Ausnahme", "GER", "AK 13/14", Gender.MALE,"4x50m Hindernisschwimmen", 2010900, ""),
            };
            default:
                return new Time[0];
        }
    }
}

