package org.lisasp.competition.results.imports.rescue2022;

import org.apache.commons.text.StringEscapeUtils;
import org.lisasp.competition.base.api.type.Gender;
import org.lisasp.competition.base.api.type.PenaltyType;
import org.lisasp.competition.base.api.type.Sex;
import org.lisasp.competition.results.api.imports.Entry;
import org.lisasp.competition.results.api.value.Penalty;
import org.lisasp.competition.results.api.value.SplitTime;
import org.lisasp.competition.results.api.value.Start;
import org.lisasp.competition.results.api.value.Swimmer;
import org.lisasp.competition.results.imports.rescue2022.model.result.MemField;
import org.lisasp.competition.results.imports.rescue2022.model.result.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.lisasp.competition.results.imports.rescue2022.NameHelper.fixName;

class EntryCreator {

    private final Sex sex;

    EntryCreator(Gender gender) {
        sex = switch (gender) {
            case Female -> Sex.Female;
            case Male -> Sex.Male;
            default -> Sex.Unknown;
        };
    }

    public Entry toEntry(org.lisasp.competition.results.imports.rescue2022.model.result.Entry entry) {
        try {
            return Entry.builder()
                        .name(fixName(entry.getPlaName() + " " + entry.getPlaSurname()))
                        .club(fixName(entry.getTeamDescrIta()))
                        .nationality(entry.getPlaNat())
                        .timeInMillis(extractTime(entry.getMemPrest()))
                        .penalties(extractPenalties(entry.getPlaCls()))
                        .placeInHeat(extractPlaceInHeat(entry))
                        .start(extractStart(entry))
                        .number(extractNumber(entry))
                        .swimmer(extractSwimmers(entry.getPlayers()))
                        .splitTimes(extractSplitTimes(entry))
                        .build();
        } catch (NullPointerException npe) {
            System.out.println(entry);
            throw npe;
        }
    }

    private SplitTime[] extractSplitTimes(org.lisasp.competition.results.imports.rescue2022.model.result.Entry entry) {
        MemField[] filledFields = Arrays.stream(entry.getMemFields())
                                        .skip(1)
                                        .filter(mf -> mf.getV() != null && !mf.getV().isBlank() && mf.getV().contains("."))
                                        .toArray(MemField[]::new);
        if (filledFields.length <= 1) {
            return new SplitTime[0];
        }
        List<SplitTime> times = new ArrayList<>();
        for (byte x = 0; x < filledFields.length - 1; x++) {
            times.add(new SplitTime(x, extractTime(filledFields[x].getV()), parseToByte(filledFields[x].getP())));
        }
        return times.toArray(SplitTime[]::new);
    }

    private Swimmer[] extractSwimmers(Player[] players) {
        if (players == null) {
            return new Swimmer[0];
        }
        return Arrays.stream(players).map(p -> toSwimmer(p, sex)).toArray(Swimmer[]::new);
    }

    private Swimmer toSwimmer(Player p, Sex sex) {
        return new Swimmer(p.getPlaCod(), fixName(p.getPlaName()), fixName(p.getPlaSurname()), sex, p.getPlaBirth());
    }

    private String extractNumber(org.lisasp.competition.results.imports.rescue2022.model.result.Entry entry) {
        return entry.getPlaTeamCod();
    }

    private Start extractStart(org.lisasp.competition.results.imports.rescue2022.model.result.Entry entry) {
        String heat = entry.getMemRBatt();
        byte lane = parseToByte(entry.getPlaLane());
        return new Start(heat, lane);
    }

    private static byte parseToByte(String value) {
        return Byte.parseByte(value);
    }

    private byte extractPlaceInHeat(org.lisasp.competition.results.imports.rescue2022.model.result.Entry entry) {
        MemField[] filledFields = Arrays.stream(entry.getMemFields()).filter(mf -> mf.getV() != null && !mf.getV().isBlank()).toArray(MemField[]::new);
        if (filledFields.length < 1) {
            return 0;
        }
        MemField lastField = filledFields[filledFields.length - 1];
        String value = lastField.getP();
        if (value == null || value.isBlank()) {
            return 0;
        }
        return Byte.parseByte(value);
    }

    private Penalty[] extractPenalties(String penalty) {
        if (penalty == null || penalty.isBlank()) {
            return new Penalty[0];
        }
        PenaltyType type = switch (penalty) {
            case "DNF", "NT" -> PenaltyType.DidNotFinish;
            case "DNS", "ASS", "ABS" -> PenaltyType.DidNotStart;
            case "DSQ" -> PenaltyType.Disqualified;
            default -> PenaltyType.None;
        };
        if (type == PenaltyType.None) {
            return new Penalty[0];
        }
        return new Penalty[]{new Penalty(penalty, type)};
    }

    private static final Set<String> keywords = Stream.of("DNS", "DSQ", "DNF", "ASS", "ABS", "NT").collect(Collectors.toSet());

    private int extractTime(String time) {
        if (time == null || time.isBlank() || keywords.contains(time)) {
            return 0;
        }
        int minutes = 0;
        if (time.contains(":")) {
            minutes = Integer.parseInt(time, 0, time.indexOf(':'), 10);
            time = time.substring(time.indexOf(':') + 1);
        }
        String[] parts = time.split("\\.");
        int seconds = Integer.parseInt(parts[0]);
        int hundredth = Integer.parseInt(parts[1]);

        return ((minutes * 60 + seconds) * 100 + hundredth) * 10;
    }
}
