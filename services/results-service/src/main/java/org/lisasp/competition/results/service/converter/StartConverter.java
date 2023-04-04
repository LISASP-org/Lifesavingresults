package org.lisasp.competition.results.service.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import org.lisasp.competition.results.api.value.Start;

public class StartConverter implements AttributeConverter<Start, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Start start) {
        if (start == null) {
            return null;
        }
        try {
            return mapper.writeValueAsString(start);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Start convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        try {
            return mapper.readValue(s, Start.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
