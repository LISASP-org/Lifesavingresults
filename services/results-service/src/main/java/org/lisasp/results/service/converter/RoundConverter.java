package org.lisasp.results.service.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import org.lisasp.results.api.value.Round;

public class RoundConverter implements AttributeConverter<Round, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Round round) {
        if (round == null) {
            return null;
        }
        try {
            return mapper.writeValueAsString(round);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Round convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        try {
            return mapper.readValue(s, Round.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
