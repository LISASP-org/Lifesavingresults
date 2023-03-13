package org.lisasp.competition.results.service;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.lisasp.basics.spring.jpa.BaseEntity;
import org.lisasp.competition.results.api.type.EventType;
import org.lisasp.competition.results.api.type.Gender;
import org.lisasp.competition.results.api.type.InputValueType;
import org.lisasp.competition.results.api.value.Round;
import org.lisasp.competition.results.service.converter.RoundConverter;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString(exclude = {"competition", "entries"})
@NoArgsConstructor
public class EventEntity extends BaseEntity {
    public EventEntity(CompetitionEntity competition) {
        this.competition = competition;
    }

    @ManyToOne
    private CompetitionEntity competition;
    private String agegroup;
    private EventType eventType;
    private Gender gender;
    private String discipline;
    @Convert(converter = RoundConverter.class)
    @Column(columnDefinition = "text", length = 100)
    private Round round;
    private InputValueType inputValueType;
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private List<EntryEntity> entries;

    public boolean matches(EventType eventType, String agegroup, Gender gender, String discipline, Round round, InputValueType inputValueType) {
        return Objects.equals(eventType, eventType) &&
                Objects.equals(this.agegroup, agegroup) &&
                Objects.equals(this.gender, gender) &&
                Objects.equals(this.discipline, discipline) &&
                Objects.equals(this.round, round) &&
                Objects.equals(this.inputValueType, inputValueType);
    }
}
