package org.lisasp.competition.results.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.lisasp.competition.base.api.type.EventType;
import org.lisasp.competition.base.api.type.Gender;
import org.lisasp.competition.base.api.type.InputValueType;
import org.lisasp.competition.results.api.value.Round;

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
