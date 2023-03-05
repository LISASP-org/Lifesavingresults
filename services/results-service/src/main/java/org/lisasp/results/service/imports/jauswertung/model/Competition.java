package org.lisasp.results.service.imports.jauswertung.model;

import lombok.Data;

@Data
public class Competition {
    private String name;
    private Event[] events;
}
