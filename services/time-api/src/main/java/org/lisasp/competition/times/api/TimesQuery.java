package org.lisasp.competition.times.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lisasp.competition.base.api.type.EventType;
import org.lisasp.competition.base.api.type.Gender;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimesQuery {
    private EventType eventType;
    private String agegroup;
    private Gender gender;
    private String discipline;
}
