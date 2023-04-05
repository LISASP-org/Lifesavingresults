package org.lisasp.competition.test.results.service.converter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.lisasp.competition.results.api.value.SplitTime;
import org.lisasp.competition.results.service.converter.SplitTimeArrayConverter;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SplitTimeArrayConverterTests {

    private final SplitTimeArrayConverter converter = new SplitTimeArrayConverter();

    @ParameterizedTest
    @ValueSource(strings = {"", "-", "{ abcdef"})
    void nonJsonInputThrowsException(String input) {
        assertThrows(RuntimeException.class, () -> converter.convertToEntityAttribute(input));
    }

    @Test
    void nullStringTest() {
        SplitTime[] actual = converter.convertToEntityAttribute(null);

        assertArrayEquals(new SplitTime[0], actual);
    }

    @Test
    void nullValueTest() {
        String value = converter.convertToDatabaseColumn(null);
        SplitTime[] actual = converter.convertToEntityAttribute(value);

        assertArrayEquals(new SplitTime[0], actual);
    }

    @Test
    void emptyArrayTest() {
        SplitTime[] original = new SplitTime[0];
        String value = converter.convertToDatabaseColumn(original);
        SplitTime[] actual = converter.convertToEntityAttribute(value);

        assertArrayEquals(original, actual);
    }

    @Test
    void singleEntryTest() {
        SplitTime[] original = {new SplitTime((byte) 0, 12345, (byte) 1)};
        String value = converter.convertToDatabaseColumn(original);
        SplitTime[] actual = converter.convertToEntityAttribute(value);

        assertArrayEquals(original, actual);
    }

    @Test
    void twoEntriesTest() {
        SplitTime[] original = {new SplitTime((byte) 0, 12345, (byte) 1), new SplitTime((byte) 1, 23456, (byte) 3)};
        String value = converter.convertToDatabaseColumn(original);
        SplitTime[] actual = converter.convertToEntityAttribute(value);

        assertArrayEquals(original, actual);
    }
}
