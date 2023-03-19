package org.lisasp.competition.results.api.imports;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record Competition(String name, String acronym, LocalDate from, LocalDate till, Event[] events) {}
