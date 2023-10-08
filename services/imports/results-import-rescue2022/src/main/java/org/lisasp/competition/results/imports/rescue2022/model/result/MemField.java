package org.lisasp.competition.results.imports.rescue2022.model.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MemField {
    @JsonProperty("V")
    private String v;
    @JsonProperty("T")
    private String t;
    @JsonProperty("P")
    private String p;
}
