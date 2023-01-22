package org.lisasp.results.api.dto;

import java.time.LocalDate;

public record CreateCompetition(String name, String acronym, LocalDate from, LocalDate till) {
}
