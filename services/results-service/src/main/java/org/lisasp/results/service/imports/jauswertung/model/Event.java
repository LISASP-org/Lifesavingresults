package org.lisasp.results.service.imports.jauswertung.model;

import lombok.Getter;

@Getter
public class Event {
    private String agegroup;
    private CompetitorType competitorType;
    private String sex;
    private String discipline;
    private byte round;
    private boolean isFinal;
    private ValueTypes valueType;
    private Entry[] times;

    public void setIsFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }
}
