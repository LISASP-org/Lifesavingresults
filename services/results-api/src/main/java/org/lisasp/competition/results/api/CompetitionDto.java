package org.lisasp.competition.results.api;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CompetitionDto(String id, int version, String name, String acronym, LocalDate from, LocalDate till) {
}
