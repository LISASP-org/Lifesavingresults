package org.lisasp.results.service.imports.jauswertung.model;

import lombok.Data;

@Data
public class Entry {
    private String startnumber;
    private String name;
    private String organization;
    private int value;
    private Penalty[] penalties;
    private Swimmer[] swimmer;
    private Start start;
}
