package org.lisasp.results.imports.rescue2022.result.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Player {

    @JsonProperty("PlaCod")
    private String PlaCod;
    @JsonProperty("PlaNat")
    private String PlaNat;
    @JsonProperty("PlaSurname")
    private String PlaSurname;
    @JsonProperty("PlaName")
    private String PlaName;
    @JsonProperty("PlaBirth")
    private String PlaBirth;
    @JsonProperty("PlaPrest")
    private String PlaPrest;
    @JsonProperty("PlaInt1")
    private String PlaInt1;
    @JsonProperty("PlaInt2")
    private String PlaInt2;
}
