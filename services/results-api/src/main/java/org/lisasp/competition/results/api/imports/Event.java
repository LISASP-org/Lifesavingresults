package org.lisasp.competition.results.api.imports;

import lombok.Builder;
import lombok.Data;
import org.lisasp.competition.base.api.type.EventType;
import org.lisasp.competition.base.api.type.Gender;
import org.lisasp.competition.base.api.type.InputValueType;
import org.lisasp.competition.results.api.value.Round;

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
