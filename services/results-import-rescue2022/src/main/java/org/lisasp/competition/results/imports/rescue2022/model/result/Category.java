package org.lisasp.competition.results.imports.rescue2022.model.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Category {
    @JsonProperty(value = "Cod")
    private String code;
    @JsonProperty(value = "Ita")
    private String italian;
    @JsonProperty(value = "Eng")
    private String english;
    @JsonProperty(value = "Fra")
    private String french;
}
