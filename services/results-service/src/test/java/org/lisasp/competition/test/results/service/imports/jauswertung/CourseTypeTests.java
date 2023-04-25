package org.lisasp.competition.test.results.service.imports.jauswertung;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.lisasp.competition.base.api.type.CourseType;
import org.lisasp.competition.results.service.imports.jauswertung.CourseTypeConverter;
import org.lisasp.competition.results.service.imports.jauswertung.DateRange;

import static org.junit.jupiter.api.Assertions.*;

class CourseTypeTests {

    private final CourseTypeConverter converter = new CourseTypeConverter();

    @Test
    void nullValue() {
        CourseType actual = converter.fromString(null);

        assertEquals(CourseType.Unknown, actual);
    }

    @Test
    void emtpyValue() {
        CourseType actual = converter.fromString("");

        assertEquals(CourseType.Unknown, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "abcdef", "25 50",  "Left Pool: 17m, right Pool: 86m"})
    void randomValues(String value) {
        CourseType actual = converter.fromString(value);

        assertEquals(CourseType.Unknown, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = { "10 yards", "Length: 49m", "Length: 32 m"})
    void randomNumberValues(String value) {
        CourseType actual = converter.fromString(value);

        assertEquals(CourseType.Other, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"50 ", "50m", "50 m", "Length: 50m", "Length: 50 m", "Left Pool: 50m, right Pool: 50m"})
    void fiftyMetres(String value) {
        CourseType actual = converter.fromString(value);

        assertEquals(CourseType.Long, actual);
    }
}
