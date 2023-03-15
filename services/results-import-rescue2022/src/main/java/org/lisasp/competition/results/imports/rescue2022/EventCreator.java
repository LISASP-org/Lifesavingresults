package org.lisasp.competition.results.imports.rescue2022;

import lombok.RequiredArgsConstructor;
import org.lisasp.competition.results.api.imports.Entry;
import org.lisasp.competition.results.api.imports.Event;
import org.lisasp.competition.base.api.type.EventType;
import org.lisasp.competition.base.api.type.Gender;
import org.lisasp.competition.base.api.type.InputValueType;
import org.lisasp.competition.base.api.type.RoundType;
import org.lisasp.competition.results.api.value.Round;
import org.lisasp.competition.results.imports.rescue2022.model.result.ResultFile;

import java.util.Arrays;
import java.util.Locale;

@RequiredArgsConstructor
public class EventCreator {

    private final String defaultAgegroup;
    private EntryCreator entryCreator;

    public Event parse(ResultFile resultFile) {
        Gender gender = extractGender(resultFile);
        entryCreator = new EntryCreator(gender);
        return Event.builder()
                    .discipline(extractDiscipline(resultFile))
                    .gender(gender)
                    .eventType(extractEventType(resultFile))
                    .agegroup(extractAgegroup(resultFile))
                    .inputValueType(extractInputValueType(resultFile))
                    .round(extractRound(resultFile))
                    .entries(extractEntries(resultFile))
                    .build();
    }

    private Entry[] extractEntries(ResultFile resultFile) {
        return Arrays.stream(resultFile.getEntries()).filter(e -> e.getPlaCls() != null).map(e -> entryCreator.toEntry(e)).toArray(Entry[]::new);
    }


    private Round extractRound(ResultFile resultFile) {
        int round = resultFile.getRound().getCode();
        RoundType type = switch (resultFile.getRound().getEnglish()) {
            case "Final" -> RoundType.Final;
            case "Swim-Off" -> RoundType.SwimOff;
            default -> RoundType.Heat;
        };
        return new Round((byte) round, type);
    }

    private InputValueType extractInputValueType(ResultFile resultFile) {
        String value = resultFile.getEntries()[0].getMemPrest();
        if (value == null || value.isBlank()) {
            return InputValueType.Rank;
        }
        return InputValueType.Time;
    }

    private String extractAgegroup(ResultFile resultFile) {
        String value = resultFile.getEntries()[0].getPlaCat();
        if (value == null || value.isBlank() || value.equals("JJ")) {
            return defaultAgegroup;
        }
        return value;
    }

    private EventType extractEventType(ResultFile resultFile) {
        return resultFile.getCompetition().getNStaff() == 0 ? EventType.Individual : EventType.Team;
    }

    private Gender extractGender(ResultFile resultFile) {
        String value = resultFile.getCategory().getEnglish().toLowerCase(Locale.ROOT);
        return switch (value) {
            case "women" -> Gender.Female;
            case "men" -> Gender.Male;
            case "mixed" -> Gender.Mixed;
            default -> Gender.Unknown;
        };
    }

    private String extractDiscipline(ResultFile result) {
        return result.getCompetition().getEnglish();
    }
}
