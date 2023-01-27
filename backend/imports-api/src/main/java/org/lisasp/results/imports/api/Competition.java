package org.lisasp.results.imports.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
@AllArgsConstructor
public class Competition {

    private String name;
    private String acronym;
    private LocalDate from;
    private LocalDate till;
    private Event[] events;
}
