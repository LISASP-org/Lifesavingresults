package org.lisasp.competition.results.imports.rescue2022.model.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExtraDetails {
    @JsonProperty(value = "Ita")
    private Field[] italian;
    @JsonProperty(value = "Eng")
    private Field[] english;
    @JsonProperty(value = "Fra")
    private Field[] french;
}
