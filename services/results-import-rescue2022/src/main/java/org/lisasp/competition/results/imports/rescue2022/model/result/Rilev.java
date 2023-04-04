package org.lisasp.competition.results.imports.rescue2022.model.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Rilev {
    @JsonProperty(value = "Num")
    private String number;
    @JsonProperty(value = "Name")
    private String name;
    @JsonProperty(value = "NameIta")
    private String italian;
    @JsonProperty(value = "NameEng")
    private String english;
    @JsonProperty(value = "NameFra")
    private String french;
}
