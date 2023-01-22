package org.lisasp.results.api;

import lombok.Builder;
import lombok.Data;
import org.lisasp.results.api.type.EventTypes;
import org.lisasp.results.api.type.Genders;
import org.lisasp.results.api.type.InputValueTypes;

@Data
@Builder
public class Event {
    private String id;
    private int version;
    private String agegroup;
    private EventTypes eventType;
    private Genders gender;
    private String discipline;
    private Round round;
    private InputValueTypes inputValueType;
    private Entry[] entries;
}
