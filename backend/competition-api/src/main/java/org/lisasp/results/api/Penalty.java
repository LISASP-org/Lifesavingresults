package org.lisasp.results.api;

import lombok.Value;
import org.lisasp.results.api.type.PenaltyType;

@Value
public class Penalty {
    private String name;
    private PenaltyType type;
    private short points;
}
