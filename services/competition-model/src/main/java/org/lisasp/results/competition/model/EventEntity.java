package org.lisasp.results.competition.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.lisasp.results.spring.jpa.BaseEntity;
import org.lisasp.results.base.api.type.EventType;
import org.lisasp.results.base.api.type.Gender;
import org.lisasp.results.base.api.type.InputValueType;
import org.lisasp.results.base.api.value.Round;
import org.lisasp.results.competition.model.converter.RoundConverter;

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
