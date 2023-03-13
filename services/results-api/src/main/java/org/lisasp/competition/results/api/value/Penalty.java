package org.lisasp.competition.results.api.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import org.lisasp.competition.results.api.type.PenaltyType;

@Value
public class Penalty {

    @JsonCreator
    public Penalty(@JsonProperty(value = "name", required = true) String name,
                   @JsonProperty(value = "type", required = true) PenaltyType type,
                   @JsonProperty(value = "points", required = true) short points) {
        this.name = name;
        this.type = type;
        this.points = points;
    }

    public Penalty(String name, PenaltyType type) {
        this(name, type, (short) 0);
    }

    private String name;
    private PenaltyType type;
    private short points;
}
