package org.lisasp.results.base.api.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import org.lisasp.results.base.api.type.PenaltyType;

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

    private String name;
    private PenaltyType type;
    private short points;
}
