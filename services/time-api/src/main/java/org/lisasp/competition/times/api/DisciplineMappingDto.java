package org.lisasp.competition.times.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisciplineMappingDto {
    private String id;
    private String discipline;
    private String agegroup;
    private String alternativeName;
}
