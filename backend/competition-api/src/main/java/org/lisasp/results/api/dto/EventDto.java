package org.lisasp.results.api.dto;

import lombok.Builder;
import lombok.Data;
import org.lisasp.results.api.Entry;
import org.lisasp.results.api.Round;
import org.lisasp.results.api.type.EventTypes;
import org.lisasp.results.api.type.Genders;
import org.lisasp.results.api.type.InputValueTypes;

@Data
@Builder
public class EventDto {
    private final String id;
    private final long version;
    private final String agegroup;
    private final EventTypes eventType;
    private final Genders gender;
    private final String discipline;
    private final Round round;
    private final InputValueTypes inputValueType;
}
