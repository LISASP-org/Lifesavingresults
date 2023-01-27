package org.lisasp.results.base.api.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class Round {
    private byte round;
    private boolean isFinal;

    @JsonCreator
    public Round(@JsonProperty(value = "round", required = true) byte round,
                   @JsonProperty(value = "isFinal", required = true) boolean isFinal) {
        this.round = round;
        this.isFinal = isFinal;
    }

    public boolean getIsFinal() {
        return isFinal;
    }

    @JsonIgnore
    public boolean isFinal() {
        return isFinal;
    }
}
