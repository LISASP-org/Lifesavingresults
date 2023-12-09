package org.lisasp.competition.results.imports.em2023;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;

public class ResultParser {

    private static final Pattern EventPattern =
            Pattern.compile("^Event (\\d+);([a-zA-Z]*), ([a-zA-Z0-9\\s]*);([a-zA-Z\\s-\\d]*)$");
    private static final Pattern RoundPattern =
            Pattern.compile("^(\\d{2})-(\\d{1,2})-2023 - \\d{2}:\\d{2};Results\\w?(.*)$");
    private static final Pattern TeamEntryPattern =
            Pattern.compile("^([a-zA-Z0-9]+)\\.;([a-zA-Z0-9/\\-\\s]+);([a-zA-Z0-9/\\-\\s]+);([:\\d\\.]*)");
    private static final Pattern TeamPenaltyPattern =
            Pattern.compile("^([a-zA-Z0-9]+);([a-zA-Z0-9/\\-\\s]+);([a-zA-Z0-9/\\-\\s]+)$");
    private static final Pattern IndividualEntryPattern = Pattern.compile(
            "^([a-zA-Z0-9]+)\\.;([a-zA-Z0-9,/\\-\\s,]+);([0-9]*);([a-zA-Z0-9/\\-\\s]+);([\\d\\.:]*)(;\\w)?(;\\d+)?(;\\w+)?([;\\d\\.]+)");
    private static final Pattern IndividualPenaltyPattern =
            Pattern.compile("^([a-zA-Z0-9]+);([a-zA-Z0-9/\\-\\s]+);([a-zA-Z0-9/\\-\\s]+)$");
    private static final Pattern FinalPattern = Pattern.compile("^Final ([a-zA-Z]+)$");
    private static final Pattern HeaderPattern = Pattern.compile("^Rank(;YB)?;Time(;Pts)?(.*)$");

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private LocalDate date;
    private String event;
    private String agegroup;
    private String gender;
    private String discipline;
    private String round;
    private String finalType;

    private String yearOfBirth;
    private int intermediateTimes;

    private final List<Entry> entries = new ArrayList<>();

    public ResultParser() {
        clear();
    }

    void push(List<String> lines) {
        int count = entries.size();
        clear();
        lines.forEach(this::push);
        if (count == entries.size()) {
            System.out.println("  No new entries added.");
        } else {
            System.out.printf("  %d new entries added.%n", entries.size() - count);
        }
    }

    private void clear() {
        date = LocalDate.of(2023, Month.SEPTEMBER, 1);
        agegroup = "";
        gender = "";
        discipline = "";
        round = "";
        finalType = "";
        event = "";
        intermediateTimes = 0;
        yearOfBirth = "";
    }

    private void push(String line) {
        if (event(line)) {
            return;
        }
        if (round(line)) {
            return;
        }
        if (headers(line)) {
            return;
        }
        if (finalType(line)) {
            return;
        }
        if (teamEntry(line)) {
            return;
        }
        if (teamEntryWithPenalty(line)) {
            return;
        }
        if (individualEntry(line)) {
            return;
        }
        if (individualEntryWithPenalty(line)) {
            return;
        }
    }

    private boolean finalType(String line) {
        Matcher matcher = FinalPattern.matcher(line);
        if (!matcher.find()) {
            return false;
        }
        finalType = matcher.group(1);
        return true;
    }

    private boolean headers(String line) {
        Matcher matcher = HeaderPattern.matcher(line);
        if (!matcher.find()) {
            return false;
        }
        intermediateTimes = matcher.group(3).replace("[a-zA-Z0-9\\s]", "").length();
        return true;
    }

    private boolean teamEntryWithPenalty(String line) {
        Matcher matcher = TeamPenaltyPattern.matcher(line);
        if (!matcher.find()) {
            return false;
        }
        String penalty = matcher.group(1);
        String name = matcher.group(2);
        String club = matcher.group(3);

        int timeInMillis = 0;
        int rank = switch (finalType) {
            case "A" -> 8;
            case "B" -> 16;
            default -> 0;
        };


        entries.add(new Entry(agegroup,
                              gender,
                              name,
                              club,
                              timeInMillis,
                              discipline,
                              rank,
                              penalty,
                              round,
                              date.format(formatter),
                              event,
                              yearOfBirth));

        return true;
    }

    private boolean individualEntryWithPenalty(String line) {
        Matcher matcher = IndividualPenaltyPattern.matcher(line);
        if (!matcher.find()) {
            return false;
        }
        String penalty = matcher.group(1);
        String name = matcher.group(2);
        String club = matcher.group(3);

        int timeInMillis = 0;
        int rank = switch (finalType) {
            case "A" -> 8;
            case "B" -> 16;
            default -> 0;
        };

        entries.add(new Entry(agegroup,
                              gender,
                              name,
                              club,
                              timeInMillis,
                              discipline,
                              rank,
                              penalty,
                              round,
                              date.format(formatter),
                              event,
                              yearOfBirth));

        return true;
    }

    private boolean teamEntry(String line) {
        Matcher matcher = TeamEntryPattern.matcher(line.replace("VZW2:", "VZW;2:"));
        if (!matcher.find()) {
            return false;
        }
        String rankString = matcher.group(1);
        String name = matcher.group(2);
        String club = matcher.group(3);
        String time = matcher.group(4);
        String penalty = "";

        int rank = Integer.parseInt(rankString);
        int timeInMillis = convertTimeString(time);

        entries.add(new Entry(agegroup,
                              gender,
                              name,
                              club,
                              timeInMillis,
                              discipline,
                              rank,
                              penalty,
                              round,
                              date.format(formatter),
                              event,
                              yearOfBirth));

        return true;
    }

    private boolean individualEntry(String line) {
        Matcher matcher = IndividualEntryPattern.matcher(line);
        if (!matcher.find()) {
            return false;
        }
        String rankString = matcher.group(1);
        String name = matcher.group(2);
        yearOfBirth = matcher.group(3);
        String club = matcher.group(4);
        String time = matcher.group(5);
        String penalty = "";

        int rank = Integer.parseInt(rankString);
        int timeInMillis = convertTimeString(time);

        entries.add(new Entry(agegroup,
                              gender,
                              name,
                              club,
                              timeInMillis,
                              discipline,
                              rank,
                              penalty,
                              round,
                              date.format(formatter),
                              event,
                              yearOfBirth));

        return true;
    }

    private static int convertTimeString(String time) {
        int minutes = 0;
        if (time.contains(":")) {
            String[] parts = time.split(":");
            minutes = (int) Math.round(Double.parseDouble(parts[0]));
            time = parts[1];
        }
        return minutes * 60000 + (int) Math.round(Double.parseDouble(time) * 1000);
    }

    private boolean round(String line) {
        Matcher matcher = RoundPattern.matcher(line);
        if (!matcher.find()) {
            return false;
        }
        int day = Integer.parseInt(matcher.group(1).trim());
        date = LocalDate.of(2023, Month.SEPTEMBER, day);
        round = matcher.group(3).trim();
        return true;
    }

    private boolean event(String line) {
        Matcher matcher = EventPattern.matcher(line);
        if (!matcher.find()) {
            return false;
        }
        clear();
        event = matcher.group(1);
        gender = matcher.group(2);
        discipline = matcher.group(3);
        agegroup = matcher.group(4);
        return true;
    }

    public Entry[] read() {
        return entries.toArray(Entry[]::new);
    }
}
