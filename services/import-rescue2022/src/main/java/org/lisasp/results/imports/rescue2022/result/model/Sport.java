package org.lisasp.results.imports.rescue2022.result.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Sport {
    @JsonProperty(value="Cod")
    private String code;
    @JsonProperty(value="CodDisciplina")
    private String codeOfDiscipline;
    @JsonProperty(value="Ita")
    private String italian;
    @JsonProperty(value="Eng")
    private String english;
    @JsonProperty(value="Fra")
    private String french;
}
