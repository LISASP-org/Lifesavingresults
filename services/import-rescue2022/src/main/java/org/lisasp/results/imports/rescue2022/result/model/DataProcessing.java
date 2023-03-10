package org.lisasp.results.imports.rescue2022.result.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DataProcessing {
    @JsonProperty(value = "Cod")
    private String code;
    @JsonProperty(value = "Name")
    private String name;
}
