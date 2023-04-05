package org.lisasp.competition.test.results.service.converter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.lisasp.competition.base.api.type.PenaltyType;
import org.lisasp.competition.results.api.value.Penalty;
import org.lisasp.competition.results.service.converter.PenaltyArrayConverter;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PenaltiesConverterTests {

    private final PenaltyArrayConverter converter = new PenaltyArrayConverter();

    @ParameterizedTest
    @ValueSource(strings = {"", "-", "{ abcdef"})
    void nonJsonInputThrowsException(String input) {
        assertThrows(RuntimeException.class, () -> converter.convertToEntityAttribute(input));
    }

    @Test
    void nullStringTest() {
        Penalty[] actual = converter.convertToEntityAttribute(null);

        assertArrayEquals(new Penalty[0], actual);
    }

    @Test
    void nullValueTest() {
        String value = converter.convertToDatabaseColumn(null);
        Penalty[] actual = converter.convertToEntityAttribute(value);

        assertArrayEquals(new Penalty[0], actual);
    }

    @Test
    void emptyArrayTest() {
        Penalty[] original = new Penalty[0];
        String value = converter.convertToDatabaseColumn(original);
        Penalty[] actual = converter.convertToEntityAttribute(value);

        assertArrayEquals(original, actual);
    }

    @Test
    void singleEntryTest() {
        Penalty[] original = {new Penalty("DSQ", PenaltyType.Disqualified)};
        String value = converter.convertToDatabaseColumn(original);
        Penalty[] actual = converter.convertToEntityAttribute(value);

        assertArrayEquals(original, actual);
    }

    @Test
    void twoEntriesTest() {
        Penalty[] original = {new Penalty("DSQ", PenaltyType.Disqualified), new Penalty("DNS", PenaltyType.DidNotStart)};
        String value = converter.convertToDatabaseColumn(original);
        Penalty[] actual = converter.convertToEntityAttribute(value);

        assertArrayEquals(original, actual);
    }
}
