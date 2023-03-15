package org.lisasp.competition.results.service;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.lisasp.basics.spring.jpa.BaseEntity;
import org.lisasp.competition.results.api.value.SplitTime;
import org.lisasp.competition.results.api.value.Penalty;
import org.lisasp.competition.results.api.value.Start;
import org.lisasp.competition.results.api.value.Swimmer;
import org.lisasp.competition.results.service.converter.PenaltyArrayConverter;
import org.lisasp.competition.results.service.converter.StartConverter;
import org.lisasp.competition.results.service.converter.SwimmerArrayConverter;
import org.lisasp.competition.results.service.converter.IntermediateTimeArrayConverter;

@Entity
@Getter
@Setter
@ToString(exclude = {"event"})
@NoArgsConstructor
public class EntryResultEntity extends BaseEntity {

    private final static Penalty[] emptyPenalties = new Penalty[0];
    private final static Swimmer[] emptySwimmers = new Swimmer[0];

    public EntryResultEntity(EventResultEntity event) {
        this.event = event;
    }

    @ManyToOne
    private EventResultEntity event;
    private String number;
    private String name;
    private String club;
    private String nationality;
    private int timeInMillis;
    private int placeInHeat;
    @Convert(converter = PenaltyArrayConverter.class)
    @Column(columnDefinition = "text", length = 1000)
    private Penalty[] penalties;
    @Convert(converter = SwimmerArrayConverter.class)
    @Column(columnDefinition = "text", length = 1000)
    private Swimmer[] swimmer;
    @Convert(converter = IntermediateTimeArrayConverter.class)
    @Column(columnDefinition = "text", length = 1000)
    private SplitTime[] splitTimes;
    @Convert(converter = StartConverter.class)
    @Column(columnDefinition = "text", length = 100)
    private Start start;

    public boolean matches(String number) {
        return this.number.equals(number);
    }

    public void fixNull() {
        if (penalties == null) {
            penalties = emptyPenalties;
        }
        if (swimmer == null) {
            swimmer = emptySwimmers;
        }
    }
}
