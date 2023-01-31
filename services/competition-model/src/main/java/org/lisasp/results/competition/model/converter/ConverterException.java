package org.lisasp.results.competition.model.converter;

import org.lisasp.results.base.api.value.Penalty;

import java.util.Arrays;

class ConverterException extends RuntimeException {
    ConverterException(Penalty[] penalties, Exception ex) {
        super(String.format("Could not serialize Penalties: %s", toString(penalties)), ex);
    }

    ConverterException(String serialized, Exception ex) {
        super(String.format("Could not deserialize Penalties: %s", serialized), ex);
    }

    private static String toString(Penalty[] penalties) {
        return Arrays.deepToString(penalties);
    }
}
