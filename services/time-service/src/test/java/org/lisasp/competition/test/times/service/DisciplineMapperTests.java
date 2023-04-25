package org.lisasp.competition.test.times.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.lisasp.competition.base.api.ChangeType;
import org.lisasp.competition.base.api.type.CourseType;
import org.lisasp.competition.base.api.type.EventType;
import org.lisasp.competition.base.api.type.Gender;
import org.lisasp.competition.times.api.TimeChangedEvent;
import org.lisasp.competition.times.service.DisciplineMapper;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DisciplineMapperTests {
    private final DisciplineMapper mapper = new DisciplineMapper();

    private TimeChangedEvent createEvent(EventType eventType, String discipline) {
        return new TimeChangedEvent("1", ChangeType.Created, "Wettkampf", "WK", eventType, "Name", "OG", "GER", "Open", Gender.Male, discipline, 12345, CourseType.Long, LocalDate.of(2023, Month.SEPTEMBER, 17));
    }

    @ParameterizedTest()
    @CsvSource({"100m Manikin Carry Fins,100m Retten einer Puppe mit Flossen",
            "100m Manikin Tow Fins,100m Retten einer Puppe mit Flossen und Gurtretter",
            "100m Obstacle Swim,100m Hindernisschwimmen",
            "100m Rescue Medley,100m kombinierte Rettungsübung",
            "200m Obstacle Swim,200m Hindernisschwimmen",
            "200m Super Lifesaver,200m Super Lifesaver",
            "4x25 Manikin Relay,4*25m Puppenstaffel",
            "4x50 Lifesaver Relay m-w,4*50m Rettungsstaffel",
            "4x50 Medley Relay,4*50m Gurtretterstaffel",
            "4x50 Obstacle Relay,4*50m Hindernisstaffel",
            "50m Manikin Carry,50m Retten einer Puppe"})
    void testOpenIndividualFemale(String input, String expected) {
        TimeChangedEvent event = createEvent(EventType.Individual, input);

        String actual = mapper.execute(event);

        assertEquals(expected, actual);
    }

}
