package org.lisasp.competition.results.service;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lisasp.basics.spring.jpa.BaseEntity;
import org.lisasp.competition.base.api.type.EventType;
import org.lisasp.competition.base.api.type.Gender;
import org.lisasp.competition.base.api.type.InputValueType;
import org.lisasp.competition.results.api.value.Round;
import org.lisasp.competition.results.service.converter.RoundConverter;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class EventResultEntity extends BaseEntity {

    public EventResultEntity(String id) {
        super(id);
    }

    public EventResultEntity(CompetitionResultEntity competition) {
        this.competition = competition;
    }

    @ManyToOne
    @JoinColumn(nullable = false, name = "competitionId")
    @NotNull
    private CompetitionResultEntity competition;
    @Column(nullable = false, length = 100)
    @NotNull
    private String agegroup;
    @Column(nullable = false, length = 12)
    @NotNull
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    @Column(nullable = false, length = 10)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(nullable = false, length = 100)
    @NotNull
    private String discipline;
    @Convert(converter = RoundConverter.class)
    @Column(columnDefinition = "text", length = 100, nullable = false)
    @NotNull
    private Round round;
    @Column(nullable = false, length = 10)
    @NotNull
    @Enumerated(EnumType.STRING)
    private InputValueType inputValueType;
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<EntryResultEntity> entries;

    public boolean matches(EventType eventType, String agegroup, Gender gender, String discipline, Round round, InputValueType inputValueType) {
        return this.eventType == eventType && Objects.equals(this.agegroup, agegroup) && this.gender == gender && Objects.equals(this.discipline, discipline) &&
               Objects.equals(this.round, round) && this.inputValueType == inputValueType;
    }

    @Override
    public String toString() {
        return String.format("EventResultEntity(id=%s, version=%d, agegroup=%s, eventType=%s, gender=%s, discipline=%s, round=%s, inputValueType=%s)",
                             getId(),
                             getVersion(),
                             agegroup,
                             eventType,
                             gender,
                             discipline,
                             round,
                             inputValueType);
    }
}
