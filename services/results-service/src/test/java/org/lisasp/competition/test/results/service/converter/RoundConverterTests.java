package org.lisasp.competition.test.results.service.converter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.lisasp.competition.base.api.type.RoundType;
import org.lisasp.competition.results.api.value.Round;
import org.lisasp.competition.results.service.converter.RoundConverter;

import static org.junit.jupiter.api.Assertions.*;

class RoundConverterTests {

    private final RoundConverter converter = new RoundConverter();

    @ParameterizedTest
    @ValueSource(strings = {"", "-", "{ abcdef"})
    void nonJsonInputThrowsException(String input) {
        assertThrows(RuntimeException.class, () -> converter.convertToEntityAttribute(input));
    }

    @Test
    void nullStringTest() {
        Round actual = converter.convertToEntityAttribute(null);

        assertNull(actual);
    }

    @Test
    void nullValueTest() {
        String value = converter.convertToDatabaseColumn(null);
        Round actual = converter.convertToEntityAttribute(value);

        assertNull(actual);
    }

    @Test
    void roundHeatTest() {
        Round original = new Round((byte) 0, RoundType.Heat);
        String value = converter.convertToDatabaseColumn(original);
        Round actual = converter.convertToEntityAttribute(value);

        assertEquals(original, actual);
    }

    @Test
    void roundFinalTest() {
        Round original = new Round((byte) 3, RoundType.Final);
        String value = converter.convertToDatabaseColumn(original);
        Round actual = converter.convertToEntityAttribute(value);

        assertEquals(original, actual);
    }
}
