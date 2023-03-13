package org.lisasp.competition.results.service.imports.jauswertung;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.lisasp.competition.results.api.type.*;
import org.lisasp.competition.results.api.value.*;
import org.lisasp.competition.results.api.imports.Entry;
import org.lisasp.competition.results.api.imports.Event;
import org.lisasp.competition.results.api.imports.Competition;
import org.lisasp.competition.results.api.imports.exception.FileFormatException;
import org.lisasp.competition.results.service.imports.jauswertung.model.CompetitorType;
import org.lisasp.competition.results.service.imports.jauswertung.model.ValueTypes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class JAuswertungImporter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Competition importJson(String json) throws FileFormatException {
        return importCompetition(deserialize(json));
    }

    private Competition importCompetition(org.lisasp.competition.results.service.imports.jauswertung.model.Competition competition) {
        Event[] events = importEvents(competition.getEvents());

        return new Competition(competition.getName(), "", null, null, events);
    }

    private Event[] importEvents(org.lisasp.competition.results.service.imports.jauswertung.model.Event[] importedEvents) {
        return Arrays.stream(importedEvents).map(e -> toEvent(e)).toArray(Event[]::new);
    }

    private Event toEvent(org.lisasp.competition.results.service.imports.jauswertung.model.Event importetEvent) {
        var inputValueType = toEntryValueType(importetEvent.getValueType());
        return Event.builder()
                .eventType(toEventType(importetEvent.getCompetitorType()))
                .discipline(importetEvent.getDiscipline())
                .round(new Round(importetEvent.getRound(), importetEvent.isFinal() ? RoundType.Final : RoundType.Heat))
                .agegroup(importetEvent.getAgegroup())
                .gender(toGender(importetEvent.getSex()))
                .inputValueType(inputValueType)
                .entries(importEntries(inputValueType, importetEvent.getTimes()))
                .build();
    }

    private Entry[] importEntries(InputValueType inputValueType, org.lisasp.competition.results.service.imports.jauswertung.model.Entry[] importedEntries) {
        return Arrays.stream(importedEntries).map(importedEntry ->
                Entry.builder()
                        .number(importedEntry.getStartnumber())
                        .name(importedEntry.getName())
                        .club(importedEntry.getOrganization())
                        .nationality("")
                        .placeInHeat(toPlace(inputValueType, importedEntry))
                        .timeInMillis(toTime(inputValueType, importedEntry))
                        .swimmer(toSwimmers(importedEntry.getSwimmer()))
                        .splitTimes(new SplitTime[0])
                        .start(toStart(importedEntry))
                        .penalties(toPenalties(importedEntry.getPenalties()))
                        .build()).toArray(Entry[]::new);
    }

    private Swimmer[] toSwimmers(org.lisasp.competition.results.service.imports.jauswertung.model.Swimmer[] swimmer) {
        if (swimmer == null) {
            return new Swimmer[0];
        }
        return Arrays.stream(swimmer)
                .map(s -> new Swimmer(s.getStartnumber(), s.getFirstName(), s.getLastName(), toSex(s.getSex()), s.getYearOfBirth()))
                .toArray(Swimmer[]::new);
    }

    private Penalty[] toPenalties(org.lisasp.competition.results.service.imports.jauswertung.model.Penalty[] penalties) {
        return Arrays.stream(penalties).map(p -> toPenalty(p)).toArray(Penalty[]::new);
    }

    private Penalty toPenalty(org.lisasp.competition.results.service.imports.jauswertung.model.Penalty p) {
        return new Penalty(p.getName(), toPenaltyType(p), p.getPoints());
    }

    private PenaltyType toPenaltyType(org.lisasp.competition.results.service.imports.jauswertung.model.Penalty p) {
        return switch (p.getType()) {
            case DidNotFinish -> PenaltyType.DidNotFinish;
            case DidNotStart -> PenaltyType.DidNotStart;
            case Disqualified -> PenaltyType.Disqualified;
            case Points -> PenaltyType.Points;
            default -> PenaltyType.None;
        };
    }

    private static Start toStart(org.lisasp.competition.results.service.imports.jauswertung.model.Entry importedEntry) {
        if (importedEntry.getStart() == null) {
            return null;
        }
        return new Start(importedEntry.getStart().getHeat(), importedEntry.getStart().getLane());
    }

    private byte toPlace(InputValueType inputValueType, org.lisasp.competition.results.service.imports.jauswertung.model.Entry importedEntry) {
        if (!inputValueType.equals(InputValueType.Rank)) {
            return (byte) 0;
        }
        return (byte) importedEntry.getValue();
    }

    private int toTime(InputValueType inputValueType, org.lisasp.competition.results.service.imports.jauswertung.model.Entry importedEntry) {
        if (!inputValueType.equals(InputValueType.Time)) {
            return 0;
        }
        return importedEntry.getValue();
    }

    private InputValueType toEntryValueType(ValueTypes valueType) {
        return valueType.equals(ValueTypes.TimeInMillis) ? InputValueType.Time : InputValueType.Rank;
    }

    private static final Set<String> male = new HashSet<>(Arrays.asList("m", "männlich", "male"));
    private static final Set<String> female = new HashSet<>(Arrays.asList("w", "weiblich", "f", "female"));
    private static final Set<String> mixed = new HashSet<>(Arrays.asList("x", "mixed"));

    private static Gender toGender(String sex) {
        if (male.contains(sex)) {
            return Gender.Male;
        }
        if (female.contains(sex)) {
            return Gender.Female;
        }
        if (mixed.contains(sex)) {
            return Gender.Mixed;
        }
        return Gender.Unknown;
    }

    private Sex toSex(String sex) {
        if (male.contains(sex)) {
            return Sex.Male;
        }
        if (female.contains(sex)) {
            return Sex.Female;
        }
        return Sex.Unknown;
    }

    private EventType toEventType(CompetitorType competitorType) {
        return competitorType.equals(CompetitorType.Team) ? EventType.Team : EventType.Individual;
    }

    private org.lisasp.competition.results.service.imports.jauswertung.model.Competition deserialize(String json) throws FileFormatException {
        try {
            return objectMapper.readValue(json, org.lisasp.competition.results.service.imports.jauswertung.model.Competition.class);
        } catch (JsonProcessingException ex) {
            throw new FileFormatException(ex);
        }
    }
}
