package org.lisasp.results.api.dto;

import lombok.Builder;
import lombok.Value;
import org.lisasp.results.api.Event;

import java.time.LocalDate;

@Value
@Builder
public class CompetitionDto {

    private String id;
    private int version;
    private String name;
    private String acronym;
    private LocalDate from;
    private LocalDate till;
}
