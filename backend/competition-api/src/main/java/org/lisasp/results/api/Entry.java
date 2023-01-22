package org.lisasp.results.api;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Entry {
    private String id;
    private int version;
    private String number;
    private String name;
    private String club;
    private String nationality;
    private int timeInMillis;
    private byte placeInHeat;
    private Penalty[] penalties;
    private Swimmer[] swimmer;
    private Start start;
}
