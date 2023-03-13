package org.lisasp.competition.results.imports.rescue2022.model.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DataProcessing {
    @JsonProperty(value = "Cod")
    private String code;
    @JsonProperty(value = "Name")
    private String name;
}
