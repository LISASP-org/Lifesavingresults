package org.lisasp.competition.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCompetition {
    private String name;
    private String acronym;
    private LocalDate from;
    private LocalDate till;
}
