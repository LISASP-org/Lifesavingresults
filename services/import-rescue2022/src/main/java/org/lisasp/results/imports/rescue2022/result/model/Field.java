package org.lisasp.results.imports.rescue2022.result.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Field {
    @JsonProperty(value = "Field")
    private String field;
}
