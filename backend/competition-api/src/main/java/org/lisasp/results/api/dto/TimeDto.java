package org.lisasp.results.api.dto;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.lisasp.results.api.type.EventTypes;
import org.lisasp.results.api.type.Genders;

@Value
@AllArgsConstructor
public class TimeDto {
    private final String id;
    private final long version;
    private final EventTypes eventType;
    private final String name;
    private final String club;
    private final String nationality;
    private final String agegroup;
    private final Genders gender;
    private final String discipline;
    private final int value;
    private final String penalties;
}
