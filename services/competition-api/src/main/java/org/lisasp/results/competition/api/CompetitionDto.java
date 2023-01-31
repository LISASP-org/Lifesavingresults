package org.lisasp.results.competition.api;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class CompetitionDto {

    private String id;
    private int version;
    private String uploadId;
    private String name;
    private String acronym;
    private LocalDate from;
    private LocalDate till;

    public CompetitionDto withoutUploadId() {
        return new CompetitionDto(id, version, "", name, acronym, from, till);
    }
}
