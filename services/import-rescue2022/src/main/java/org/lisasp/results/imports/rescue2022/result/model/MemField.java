package org.lisasp.results.imports.rescue2022.result.model;

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
