package org.lisasp.results.competition.api;

import java.time.LocalDate;

public record CreateCompetition(String name, String acronym, LocalDate from, LocalDate till) {
}
