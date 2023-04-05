package org.lisasp.competition.test.results.service.converter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.lisasp.competition.results.api.value.Start;
import org.lisasp.competition.results.service.converter.StartConverter;

import static org.junit.jupiter.api.Assertions.*;

class StartConverterTest {

    private final StartConverter converter = new StartConverter();

    @ParameterizedTest
    @ValueSource(strings = {"", "-", "{ abcdef"})
    void nonJsonInputThrowsException(String input) {
        assertThrows(RuntimeException.class, () -> converter.convertToEntityAttribute(input));
    }

    @Test
    void nullStringTest() {
        Start actual = converter.convertToEntityAttribute(null);

        assertNull(actual);
    }

    @Test
    void nullValueTest() {
        String value = converter.convertToDatabaseColumn(null);
        Start actual = converter.convertToEntityAttribute(value);

        assertNull(actual);
    }

    @Test
    void startLane1Test() {
        Start original = new Start("123", (byte) 1);
        String value = converter.convertToDatabaseColumn(original);
        Start actual = converter.convertToEntityAttribute(value);

        assertEquals(original, actual);
    }

    @Test
    void startLane3Test() {
        Start original = new Start("45", (byte) 3);
        String value = converter.convertToDatabaseColumn(original);
        Start actual = converter.convertToEntityAttribute(value);

        assertEquals(original, actual);
    }
}
