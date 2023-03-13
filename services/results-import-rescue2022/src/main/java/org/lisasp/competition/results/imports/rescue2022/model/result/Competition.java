package org.lisasp.competition.results.imports.rescue2022.model.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Competition {
    @JsonProperty(value="Cod")
    private String code;
    @JsonProperty(value="Type")
    private String type;
    @JsonProperty(value="Ita")
    private String italian;
    @JsonProperty(value="Eng")
    private String english;
    @JsonProperty(value="Fra")
    private String french;
    @JsonProperty(value="Int")
    private String number;
    @JsonProperty(value="NStaff")
    private int nStaff;
    @JsonProperty(value="NumGiudici")
    private String numGiudici;
}
