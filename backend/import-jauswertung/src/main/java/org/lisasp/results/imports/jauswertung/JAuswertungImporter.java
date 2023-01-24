package org.lisasp.results.imports.jauswertung;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.lisasp.results.api.*;
import org.lisasp.results.api.type.EventTypes;
import org.lisasp.results.api.type.Genders;
import org.lisasp.results.api.type.InputValueTypes;
import org.lisasp.results.api.type.PenaltyType;
import org.lisasp.results.imports.jauswertung.model.CompetitorType;
import org.lisasp.results.imports.jauswertung.model.ValueTypes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class JAuswertungImporter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Competition importJson(String json) throws ImportFormatException {
        return importCompetition(deserialize(json));
    }

    @NotNull
    public Competition importCompetition(org.lisasp.results.imports.jauswertung.model.Competition competition) {
        Event[] events = importEvents(competition.getEvents());

        return new Competition(competition.getName(), "", null, null, events);
    }

    private Event[] importEvents(org.lisasp.results.imports.jauswertung.model.Event[] importedEvents) {
        return Arrays.stream(importedEvents).map(e -> toEvent(e)).toArray(Event[]::new);
    }

    private Event toEvent(org.lisasp.results.imports.jauswertung.model.Event importetEvent) {
        var inputValueType = toEntryValueType(importetEvent.getValueType());
        return Event.builder()
                .eventType(toEventType(importetEvent.getCompetitorType()))
                .round(new Round(importetEvent.getRound(), importetEvent.isFinal()))
                .agegroup(importetEvent.getAgegroup())
                .gender(toGender(importetEvent.getSex()))
                .inputValueType(inputValueType)
                .entries(importEntries(inputValueType, importetEvent.getTimes()))
                .build();
    }

    private Entry[] importEntries(InputValueTypes inputValueType, org.lisasp.results.imports.jauswertung.model.Entry[] importedEntries) {
        return Arrays.stream(importedEntries).map(importedEntry ->
                Entry.builder()
                        .number(importedEntry.getStartnumber())
                        .name(importedEntry.getName())
                        .club(importedEntry.getOrganization())
                        .nationality("")
                        .placeInHeat(toPlace(inputValueType, importedEntry))
                        .timeInMillis(toTime(inputValueType, importedEntry))
                        .start(toStart(importedEntry))
                        .penalties(toPenalties(importedEntry.getPenalties()))
                        .build()).toArray(Entry[]::new);
    }

    private Penalty[] toPenalties(org.lisasp.results.imports.jauswertung.model.Penalty[] penalties) {
        return Arrays.stream(penalties).map(p -> toPenalty(p)).toArray(Penalty[]::new);
    }

    private Penalty toPenalty(org.lisasp.results.imports.jauswertung.model.Penalty p) {
        return new Penalty(p.getName(), toPenaltyType(p), p.getPoints());
    }

    private PenaltyType toPenaltyType(org.lisasp.results.imports.jauswertung.model.Penalty p) {
        return switch (p.getType()) {
            case DidNotFinish -> PenaltyType.DidNotFinish;
            case DidNotStart -> PenaltyType.DidNotStart;
            case Disqualified -> PenaltyType.Disqualified;
            case Points -> PenaltyType.Points;
            default -> PenaltyType.None;
        };
    }

    private static Start toStart(org.lisasp.results.imports.jauswertung.model.Entry importedEntry) {
        if (importedEntry.getStart() == null) {
            return null;
        }
        return new Start(importedEntry.getStart().getHeat(), importedEntry.getStart().getLane());
    }

    private byte toPlace(InputValueTypes inputValueType, org.lisasp.results.imports.jauswertung.model.Entry importedEntry) {
        if (!inputValueType.equals(InputValueTypes.Rank)) {
            return (byte)0;
        }
        return (byte)importedEntry.getValue();
    }

    private int toTime(InputValueTypes inputValueType, org.lisasp.results.imports.jauswertung.model.Entry importedEntry) {
        if (!inputValueType.equals(InputValueTypes.Time)) {
            return 0;
        }
        return importedEntry.getValue();
    }

    private InputValueTypes toEntryValueType(ValueTypes valueType) {
        if (valueType.equals(ValueTypes.TimeInMillis)) {
            return InputValueTypes.Time;
        }
        return InputValueTypes.Rank;
    }

    private static final Set<String> male = new HashSet<>(Arrays.asList("m", "männlich", "male"));
    private static final Set<String> female = new HashSet<>(Arrays.asList("w", "weiblich", "f", "female"));
    private static final Set<String> mixed = new HashSet<>(Arrays.asList("x", "mixed"));

    private static Genders toGender(String sex) {
        if (male.contains(sex))
            return Genders.MALE;
        if (female.contains(sex))
            return Genders.FEMALE;
        if (mixed.contains(sex)) {
            return Genders.MIXED;
        }
        return Genders.UNKNOWN;
    }

    private EventTypes toEventType(CompetitorType competitorType) {
        return competitorType.equals(CompetitorType.Team) ? EventTypes.TEAM : EventTypes.INDIVIDUAL;
    }

    private org.lisasp.results.imports.jauswertung.model.Competition deserialize(String json) throws ImportFormatException {
        try {
            return objectMapper.readValue(json, org.lisasp.results.imports.jauswertung.model.Competition.class);
        } catch (JsonProcessingException ex) {
            throw new ImportFormatException(ex);
        }
    }
}
