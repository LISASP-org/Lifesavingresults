package org.lisasp.competition.results.api.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import org.lisasp.competition.base.api.type.RoundType;

@Value
public class Round {
    private byte round;
    private RoundType type;

    @JsonCreator
    public Round(@JsonProperty(value = "round", required = true) byte round,
                 @JsonProperty(value = "type", required = true) RoundType type) {
        this.round = round;
        this.type = type;
    }

    public RoundType getType() {
        return type;
    }
}
