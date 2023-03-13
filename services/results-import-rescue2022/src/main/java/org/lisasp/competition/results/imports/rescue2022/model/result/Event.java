package org.lisasp.competition.results.imports.rescue2022.model.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Event {
    @JsonProperty(value="Date")
    private String code;
    @JsonProperty(value="Part")
    private String italian;
    @JsonProperty(value="Comunicate")
    private String english;
    @JsonProperty(value="Audience")
    private String french;
    @JsonProperty(value="Place")
    private String place;
    @JsonProperty(value="Time")
    private String time;
    @JsonProperty(value="Ord")
    private String ord;
}
