package org.lisasp.competition.api.dto;

import java.time.LocalDate;

public record CreateCompetition(String name, String acronym, LocalDate from, LocalDate till) {
}
