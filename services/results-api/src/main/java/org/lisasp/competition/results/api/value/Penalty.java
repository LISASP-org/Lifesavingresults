package org.lisasp.competition.results.api.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.lisasp.competition.base.api.type.PenaltyType;

public record Penalty(String name, PenaltyType type, short points) {

    public Penalty(String name, PenaltyType type) {
        this(name, type, (short) 0);
    }

    @JsonCreator
    public Penalty(@JsonProperty(value = "name", required = true) String name,
                   @JsonProperty(value = "type", required = true) PenaltyType type,
                   @JsonProperty(value = "points", required = true) short points) {
        this.name = name;
        this.type = type;
        this.points = points;
    }
}
