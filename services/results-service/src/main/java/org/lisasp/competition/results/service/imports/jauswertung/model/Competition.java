package org.lisasp.competition.results.service.imports.jauswertung.model;

import lombok.Data;
import org.lisasp.competition.base.api.type.CourseType;

@Data
public class Competition {
    private String name;
    private String lengthOfCourse;
    private String date;
    private Event[] events;
}
