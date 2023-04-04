package org.lisasp.competition.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Builder
public record CompetitionDto(String id, int version, String name, String acronym, LocalDate from, LocalDate till) {

}
