package org.lisasp.results.service.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import org.lisasp.results.api.value.Swimmer;

public class SwimmerArrayConverter implements AttributeConverter<Swimmer[], String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Swimmer[] swimmers) {
        if (swimmers == null) {
            swimmers = new Swimmer[0];
        }
        try {
            return mapper.writeValueAsString(swimmers);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Swimmer[] convertToEntityAttribute(String s) {
        if (s == null) {
            return new Swimmer[0];
        }
        try {
            return mapper.readValue(s, Swimmer[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
