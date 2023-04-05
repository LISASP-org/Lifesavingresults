package org.lisasp.competition.results.service.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import org.lisasp.competition.results.api.value.SplitTime;

public class SplitTimeArrayConverter implements AttributeConverter<SplitTime[], String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(SplitTime[] times) {
        if (times == null) {
            times = new SplitTime[0];
        }
        try {
            return mapper.writeValueAsString(times);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SplitTime[] convertToEntityAttribute(String s) {
        if (s == null) {
            return new SplitTime[0];
        }
        try {
            return mapper.readValue(s, SplitTime[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
