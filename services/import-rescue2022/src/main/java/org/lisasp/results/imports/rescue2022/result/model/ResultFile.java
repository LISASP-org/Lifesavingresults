package org.lisasp.results.imports.rescue2022.result.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(value = {"Export", "Sport", "Competition", "Document", "Category", "Round", "Heat", "DataProcessing", "Rilev", "Event", "ExtraDetails", "data"})
public class ResultFile {

    private String jsonfilename;
    private String counter;
    private String tipologia;
    private String visSoc;
    private String visNaz;
    private String visCat;
    private String visPunti;
    @JsonProperty(value = "Scorpora")
    private String Scorpora;
    private String pdf;
    @JsonProperty(value = "StatoBatt")
    private String StatoBatt;
}
