package org.lisasp.results.model.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import org.lisasp.results.base.api.value.Penalty;

public class PenaltyArrayConverter implements AttributeConverter<Penalty[], String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Penalty[] penalties) {
        if (penalties == null) {
            penalties = new Penalty[0];
        }
        try {
            return mapper.writeValueAsString(penalties);
        } catch (JsonProcessingException e) {
            throw new ConverterException(penalties, e);
        }
    }

    @Override
    public Penalty[] convertToEntityAttribute(String serialized) {
        if (serialized == null) {
            return new Penalty[0];
        }
        try {
            return mapper.readValue(serialized, Penalty[].class);
        } catch (JsonProcessingException e) {
            throw new ConverterException(serialized, e);
        }
    }
}
