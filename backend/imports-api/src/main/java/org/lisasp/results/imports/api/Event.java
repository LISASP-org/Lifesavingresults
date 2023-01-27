package org.lisasp.results.imports.api;

import lombok.Builder;
import lombok.Data;
import org.lisasp.results.base.api.type.EventType;
import org.lisasp.results.base.api.type.Gender;
import org.lisasp.results.base.api.type.InputValueType;
import org.lisasp.results.base.api.value.Round;

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
