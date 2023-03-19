package org.lisasp.competition.results.api.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Start(String heat, byte lane) {

    @JsonCreator
    public Start(@JsonProperty(value = "heat", required = true) String heat, @JsonProperty(value = "lane", required = true) byte lane) {
        this.heat = heat;
        this.lane = lane;
    }
}
