package org.lisasp.competition.test.times.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.lisasp.competition.base.api.ChangeType;
import org.lisasp.competition.base.api.type.CourseType;
import org.lisasp.competition.base.api.type.EventType;
import org.lisasp.competition.base.api.type.Gender;
import org.lisasp.competition.times.api.TimeChangedEvent;
import org.lisasp.competition.times.service.AgegroupMapper;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AgegroupMapperTests {
    private final AgegroupMapper mapper = new AgegroupMapper();

    private TimeChangedEvent createEvent(EventType eventType, String agegroup, Gender gender) {
        return new TimeChangedEvent("1", ChangeType.Created, "Wettkampf", "WK", eventType, "Name", "OG", "GER", agegroup, gender, "200m Obstacle Swim", 12345, CourseType.Short, LocalDate.of(2023, Month.JULY, 23));
    }

    @ParameterizedTest()
    @ValueSource(strings = {"Open", "open", "OPEN"})
    void testOpenIndividualFemale(String agegroup) {
        TimeChangedEvent event = createEvent(EventType.Individual, agegroup, Gender.Female);

        String actual = mapper.execute(event);

        assertEquals("AK Offen", actual);
    }

    @ParameterizedTest()
    @ValueSource(strings = {"Open", "open", "OPEN"})
    void testOpenIndividualMale(String agegroup) {
        TimeChangedEvent event = createEvent(EventType.Individual, agegroup, Gender.Male);

        String actual = mapper.execute(event);

        assertEquals("AK Offen", actual);
    }

    @ParameterizedTest()
    @ValueSource(strings = {"Open", "open", "OPEN"})
    void testOpenTeamFemale(String agegroup) {
        TimeChangedEvent event = createEvent(EventType.Team, agegroup, Gender.Female);

        String actual = mapper.execute(event);

        assertEquals("AK Offen", actual);
    }

    @ParameterizedTest()
    @ValueSource(strings = {"Open", "open", "OPEN"})
    void testOpenTeamMale(String agegroup) {
        TimeChangedEvent event = createEvent(EventType.Team, agegroup, Gender.Male);

        String actual = mapper.execute(event);

        assertEquals("AK Offen", actual);
    }

    @ParameterizedTest()
    @ValueSource(ints = {30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95})
    void testIndividualFemale(int value) {
        String agegroup = String.format("W%d-%d", value, value + 4);
        String expected = String.format("AK %d", value);
        TimeChangedEvent event = createEvent(EventType.Individual, agegroup, Gender.Female);

        String actual = mapper.execute(event);

        assertEquals(expected, actual);
    }

    @ParameterizedTest()
    @ValueSource(ints = {30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95})
    void testIndividualMale(int value) {
        String agegroup = String.format("M%d-%d", value, value + 4);
        String expected = String.format("AK %d", value);
        TimeChangedEvent event = createEvent(EventType.Individual, agegroup, Gender.Male);

        String actual = mapper.execute(event);

        assertEquals(expected, actual);
    }

    @ParameterizedTest()
    @ValueSource(ints = {30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95})
    void testIndividualMaleWithWrongGender(int value) {
        String agegroup = String.format("M%d-%d", value, value + 4);
        TimeChangedEvent event = createEvent(EventType.Individual, agegroup, Gender.Female);

        String actual = mapper.execute(event);

        assertEquals(null, actual);
    }

    @ParameterizedTest()
    @ValueSource(ints = {30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95})
    void testIndividualMaleWithWrongEventType(int value) {
        String agegroup = String.format("M%d-%d", value, value + 4);
        TimeChangedEvent event = createEvent(EventType.Team, agegroup, Gender.Male);

        String actual = mapper.execute(event);

        assertEquals(null, actual);
    }

    @ParameterizedTest()
    @ValueSource(ints = {30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95})
    void testIndividualFemaleWithWrongGender(int value) {
        String agegroup = String.format("W%d-%d", value, value + 4);
        TimeChangedEvent event = createEvent(EventType.Individual, agegroup, Gender.Male);

        String actual = mapper.execute(event);

        assertEquals(null, actual);
    }

    @ParameterizedTest()
    @ValueSource(ints = {30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95})
    void testIndividualFemaleWithWrongEventType(int value) {
        String agegroup = String.format("W%d-%d", value, value + 4);
        TimeChangedEvent event = createEvent(EventType.Team, agegroup, Gender.Female);

        String actual = mapper.execute(event);

        assertEquals(null, actual);
    }
}
