package org.lisasp.competition.results.service;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lisasp.basics.spring.jpa.BaseEntity;
import org.lisasp.competition.results.api.value.Penalty;
import org.lisasp.competition.results.api.value.SplitTime;
import org.lisasp.competition.results.api.value.Start;
import org.lisasp.competition.results.api.value.Swimmer;
import org.lisasp.competition.results.service.converter.SplitTimeArrayConverter;
import org.lisasp.competition.results.service.converter.PenaltyArrayConverter;
import org.lisasp.competition.results.service.converter.StartConverter;
import org.lisasp.competition.results.service.converter.SwimmerArrayConverter;

import java.util.Arrays;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class EntryResultEntity extends BaseEntity {

    private static final Penalty[] emptyPenalties = new Penalty[0];
    private static final Swimmer[] emptySwimmers = new Swimmer[0];
    private static final SplitTime[] emptySplitTimes = new SplitTime[0];

    public EntryResultEntity(String id) {
        super(id);
    }

    public EntryResultEntity(EventResultEntity event) {
        this.event = event;
    }

    @ManyToOne
    @JoinColumn(nullable = false, name = "eventId")
    @NotNull
    @Enumerated(EnumType.STRING)
    private EventResultEntity event;
    @Column(nullable = false, length = 10)
    @NotNull
    private String number;
    @Column(nullable = false, length = 100)
    @NotNull
    private String name;
    @Column(nullable = false, length = 100)
    @NotNull
    private String club;
    @Column(nullable = false, length = 3)
    @NotNull
    private String nationality;
    @Column(nullable = false)
    @NotNull
    private int timeInMillis;
    @Column(nullable = false)
    @NotNull
    private int placeInHeat;
    @Convert(converter = PenaltyArrayConverter.class)
    @Column(columnDefinition = "text", length = 1000, nullable = false)
    @NotNull
    private Penalty[] penalties;
    @Convert(converter = SwimmerArrayConverter.class)
    @Column(columnDefinition = "text", length = 1000, nullable = false)
    @NotNull
    private Swimmer[] swimmer;
    @Convert(converter = SplitTimeArrayConverter.class)
    @Column(columnDefinition = "text", length = 1000, nullable = false)
    @NotNull
    private SplitTime[] splitTimes;
    @Convert(converter = StartConverter.class)
    @Column(columnDefinition = "text", length = 100, nullable = false)
    @NotNull
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
        if (splitTimes == null) {
            splitTimes = emptySplitTimes;
        }
    }

    @Override
    public String toString() {
        return String.format(
                "EntryResultEntity(id=%s, version=%d, number=%s, name=%s, club=%s, nationality=%s, timeInMillis=%d, placeInHeat=%d, penalties=%s, swimmer=%s," +
                " splitTimes=%s, start=%s)",
                getId(),
                getVersion(),
                number,
                name,
                club,
                nationality,
                timeInMillis,
                placeInHeat,
                Arrays.toString(penalties),
                Arrays.toString(swimmer),
                Arrays.toString(splitTimes),
                start);
    }

    @Override
    protected void beforeSave() {
        super.beforeSave();
        fixNull();
    }
}
