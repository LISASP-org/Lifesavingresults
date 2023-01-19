package org.lisasp.results.competitions;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Time {
    private String id;
    private EventType eventType;
    private String name;
    private String club;
    private String nationality;
    private String agegroup;
    private Gender gender;
    private String discipline;
    private int timeInMillis;
    private String penalties;
}
