package org.lisasp.competition.times.api;

import org.lisasp.competition.base.api.ChangeType;
import org.lisasp.competition.base.api.type.CourseType;
import org.lisasp.competition.base.api.type.EventType;
import org.lisasp.competition.base.api.type.Gender;

import java.time.LocalDate;

public record TimeChangedEvent(String id, ChangeType changeType, String competition, String acronym,
                               EventType eventType,
                               String name, String club, String nationality,
                               String agegroup, Gender gender, String discipline,
                               int timeInMillis, CourseType courseType, LocalDate date) {
    public TimeDto toDto() {
        return new TimeDto(id(),
                competition(),
                acronym(),
                eventType(),
                name(),
                club(),
                nationality(),
                agegroup(),
                gender(),
                discipline(),
                timeInMillis(),
                courseType(),
                date());
    }

    public TimeChangedEvent withChangeType(ChangeType newChangeType) {
        return new TimeChangedEvent(id, newChangeType, competition, acronym, eventType, name, club, nationality, agegroup, gender, discipline, timeInMillis, courseType, date);
    }
}
