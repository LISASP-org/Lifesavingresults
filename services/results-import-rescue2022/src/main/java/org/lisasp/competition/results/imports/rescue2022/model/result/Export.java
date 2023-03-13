package org.lisasp.competition.results.imports.rescue2022.model.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Export {
    @JsonProperty(value="ExportFormat")
    private String format;
    @JsonProperty(value="ExpType")
    private String type;
    @JsonProperty(value="ExpName")
    private String name;
    @JsonProperty(value="ExpDescr")
    private String description;

}
