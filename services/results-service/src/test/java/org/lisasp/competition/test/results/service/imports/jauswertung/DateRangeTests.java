package org.lisasp.competition.test.results.service.imports.jauswertung;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.lisasp.competition.results.service.imports.jauswertung.DateRange;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class DateRangeTests {

    private LocalDate date1 = LocalDate.of(2023, Month.APRIL, 20);
    private LocalDate date2 = LocalDate.of(2023, Month.APRIL, 23);
    private LocalDate date3 = LocalDate.of(2023, Month.MAY, 9);

    @Test
    void nullValue() {
        DateRange actual = DateRange.fromString(null);

        assertNotNull(actual);
        assertNull(actual.first());
        assertNull(actual.last());
    }

    @Test
    void emptyValue() {
        DateRange actual = DateRange.fromString("");

        assertNotNull(actual);
        assertNull(actual.first());
        assertNull(actual.last());
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "20/04/2023", "2023-15-30", "2020-11-85", "2020-00-00"})
    void randomValues(String value) {
        DateRange actual = DateRange.fromString(value);

        assertNotNull(actual);
        assertNull(actual.first());
        assertNull(actual.last());
    }

    @ParameterizedTest
    @ValueSource(strings = {"2023-04-20", "20.04.2023"})
    void oneDate(String value) {
        DateRange actual = DateRange.fromString(value);

        assertNotNull(actual);
        assertEquals(date1, actual.first());
        assertEquals(date1, actual.last());
    }

    @ParameterizedTest
    @ValueSource(strings = {"20-23.04.2023"})
    void twoDates20To23(String value) {
        DateRange actual = DateRange.fromString(value);

        assertNotNull(actual);
        assertEquals(date1, actual.first());
        assertEquals(date2, actual.last());
    }

    @ParameterizedTest
    @ValueSource(strings = {"20.04.-09.05.2023"})
    void twoDatesAprilToMay(String value) {
        DateRange actual = DateRange.fromString(value);

        assertNotNull(actual);
        assertEquals(date1, actual.first());
        assertEquals(date3, actual.last());
    }
}
