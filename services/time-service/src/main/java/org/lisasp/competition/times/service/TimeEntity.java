package org.lisasp.competition.times.service;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lisasp.basics.spring.jpa.BaseEntity;
import org.lisasp.competition.base.api.type.EventType;
import org.lisasp.competition.base.api.type.Gender;
import org.lisasp.competition.times.api.TimeDto;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TimeEntity extends BaseEntity {

    public TimeEntity(String id) {
        super(id);
    }

    private String competition;
    private String acronym;
    private String agegroup;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String discipline;
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    private String name;
    private String club;
    private String nationality;
    private int timeInMillis;

    public TimeDto toDto() {
        return new TimeDto(getId(),
                           getCompetition(),
                           getAcronym(),
                           getEventType(),
                           getName(),
                           getClub(),
                           getNationality(),
                           getAgegroup(),
                           getGender(),
                           getDiscipline(),
                           getTimeInMillis());
    }

    public void updateFrom(TimeDto time) {
        setCompetition(time.competition());
        setAcronym(time.acronym());
        setEventType(time.eventType());
        setName(time.name());
        setClub(time.club());
        setNationality(time.nationality());
        setAgegroup(time.agegroup());
        setGender(time.gender());
        setDiscipline(time.discipline());
        setTimeInMillis(time.timeInMillis());
    }
}
