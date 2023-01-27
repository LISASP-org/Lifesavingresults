package org.lisasp.results.base.api.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class Start {

    @JsonCreator
    public Start(@JsonProperty(value = "heat", required = true) String heat,
                 @JsonProperty(value = "lane", required = true) byte lane) {
        this.heat = heat;
        this.lane = lane;
    }

    private String heat;
    private byte lane;
}
