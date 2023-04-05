package org.lisasp.competition.test.results.service.converter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.lisasp.competition.base.api.type.Sex;
import org.lisasp.competition.results.api.value.Penalty;
import org.lisasp.competition.results.api.value.Swimmer;
import org.lisasp.competition.results.service.converter.SwimmerArrayConverter;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SwimmerArrayConverterTests {

    private final SwimmerArrayConverter converter = new SwimmerArrayConverter();

    @ParameterizedTest
    @ValueSource(strings = {"", "-", "{ abcdef"})
    void nonJsonInputThrowsException(String input) {
        assertThrows(RuntimeException.class, () -> converter.convertToEntityAttribute(input));
    }
    
    @Test
    void nullStringTest() {
        Swimmer[] actual = converter.convertToEntityAttribute(null);

        assertArrayEquals(new Penalty[0], actual);
    }

    @Test
    void nullValueTest() {
        String value = converter.convertToDatabaseColumn(null);
        Swimmer[] actual = converter.convertToEntityAttribute(value);

        assertArrayEquals(new Swimmer[0], actual);
    }

    @Test
    void emptyArrayTest() {
        Swimmer[] original = new Swimmer[0];
        String value = converter.convertToDatabaseColumn(original);
        Swimmer[] actual = converter.convertToEntityAttribute(value);

        assertArrayEquals(original, actual);
    }

    @Test
    void singleEntryTest() {
        Swimmer[] original = {new Swimmer("123", "First", "Last", Sex.Female, (short) 2002)};
        String value = converter.convertToDatabaseColumn(original);
        Swimmer[] actual = converter.convertToEntityAttribute(value);

        assertArrayEquals(original, actual);
    }

    @Test
    void twoEntriesTest() {
        Swimmer[] original = {new Swimmer("123", "First", "Last", Sex.Female, (short) 2002), new Swimmer("234", "A", "Z", Sex.Male, (short) 2005)};
        String value = converter.convertToDatabaseColumn(original);
        Swimmer[] actual = converter.convertToEntityAttribute(value);

        assertArrayEquals(original, actual);
    }
}
