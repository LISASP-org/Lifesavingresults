package org.lisasp.results.competition.api;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.lisasp.results.base.api.type.EventType;
import org.lisasp.results.base.api.type.Gender;

@Value
@AllArgsConstructor
public class TimeDto {
    private final String id;
    private final long version;
    private final EventType eventType;
    private final String name;
    private final String club;
    private final String nationality;
    private final String agegroup;
    private final Gender gender;
    private final String discipline;
    private final int value;
    private final String penalties;
}
