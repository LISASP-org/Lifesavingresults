package org.lisasp.results.api.imports;

import lombok.Builder;
import lombok.Data;
import org.lisasp.results.api.type.EventType;
import org.lisasp.results.api.type.Gender;
import org.lisasp.results.api.type.InputValueType;
import org.lisasp.results.api.value.Round;

@Data
@Builder
public class Event {
    private String agegroup;
    private EventType eventType;
    private Gender gender;
    private String discipline;
    private Round round;
    private InputValueType inputValueType;
    private Entry[] entries;
}
