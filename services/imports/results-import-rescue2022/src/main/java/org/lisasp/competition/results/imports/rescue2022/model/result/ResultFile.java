package org.lisasp.competition.results.imports.rescue2022.model.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResultFile {

    @JsonProperty("jsonfilename")
    private String jsonFilename;
    private String counter;
    private String tipologia;
    private String visSoc;
    private String visNaz;
    private String visCat;
    private String visPunti;
    @JsonProperty("Scorpora")
    private String scorpora;
    @JsonProperty("pdf")
    private String pdfFilename;
    @JsonProperty("StatoBatt")
    private String statoBatt;
    @JsonProperty("Export")
    private Export export;
    @JsonProperty("Sport")
    private Sport sport;
    @JsonProperty("Competition")
    private Competition competition;
    @JsonProperty("Document")
    private Document document;
    @JsonProperty("Category")
    private Category category;
    @JsonProperty("Round")
    private Round round;
    @JsonProperty("Heat")
    private Heat heat;
    @JsonProperty("DataProcessing")
    private DataProcessing dataProcessing;
    @JsonProperty("Rilev")
    private Rilev rilev;
    @JsonProperty("Event")
    private Event event;
    @JsonProperty("ExtraDetails")
    private ExtraDetails extraDetails;
    @JsonProperty("data")
    private Entry[] entries;

}
