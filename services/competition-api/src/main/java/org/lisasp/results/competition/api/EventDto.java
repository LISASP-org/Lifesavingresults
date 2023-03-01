package org.lisasp.results.competition.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import org.lisasp.results.base.api.type.EventType;
import org.lisasp.results.base.api.type.Gender;
import org.lisasp.results.base.api.type.InputValueType;
import org.lisasp.results.base.api.value.Round;

@Data
@Builder
@AllArgsConstructor
public class EventDto {
    private final String id;
    private final long version;
    private final String agegroup;
    private final EventType eventType;
    private final Gender gender;
    private final String discipline;
    private final Round round;
    private final InputValueType inputValueType;
}
