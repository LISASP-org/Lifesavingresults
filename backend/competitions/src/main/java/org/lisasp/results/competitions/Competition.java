package org.lisasp.results.competitions;

import lombok.Value;

import java.time.LocalDate;

@Value
public class Competition {
    private String id;
    private String name;
    private String acronym;
    private LocalDate from;
    private LocalDate till;
}
