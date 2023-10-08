package org.lisasp.competition.results.imports.orangecup2022;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class SaturdayResultReader {

    private static final Pattern
            EventPattern = Pattern.compile("^Event (\\d+)$");
    private static final Pattern
            EventGenderDisciplineAgegroupAndDatePattern = Pattern.compile("^Event (\\d+)\\s+(\\w+), ([a-zA-Z0-9 ]*?)\\s+(\\w+)\\n(\\d{2}-\\d{2}-\\d{4})\\s+Results$");
    private static final Pattern EventGenderDisciplineAndAgegroupPattern = Pattern.compile("^Event (\\d+)\\s+(\\w+), ([a-zA-Z0-9 ]*?)\\s+(\\w+)$");
    private static final Pattern EventGenderAndDisciplinePattern = Pattern.compile("^Event (\\d+),\\s+(\\w+), ([a-zA-Z0-9 ]*?)$");
    private static final Pattern Event2Pattern = Pattern.compile("^Event (\\d+), (\\w+), (.*?), (\\w+)$");
    private static final Pattern EventAndDatePattern = Pattern.compile("^Event (\\d+)\\n(\\d{2}-\\d{2}-\\d{4})$");
    private static final Pattern EventAndDateAndWorldRecordPattern = Pattern.compile("^Event (\\d+)\\n" + "(\\d{2}-\\d" + "{2}-\\d{4})\\nWorld Record$");

    private static final Pattern HeadersAndAgegroup = Pattern.compile("^Rank\\s+YB\\s+Time\\n(\\w+)$");
    private static final Pattern RankAndName = Pattern.compile("^(\\d+)\\.\\s+(\\w+.*)$");
    private static final Pattern RankAndTimeHeaders = Pattern.compile("^Rank\\s{5,}Time$");
    private static final Pattern RankAndYBHeaders = Pattern.compile("^Rank\\s{5,}YB$");
    private static final Pattern GenderAndDiscipline = Pattern.compile("^(\\w+), ([\\w\\s]*)$");
    private static final Pattern AgegroupAndResultsHeader = Pattern.compile("^(\\w+) Results$");
    private static final Pattern DisciplinePlusRecord = Pattern.compile("^(.+)\\n[\\w\\s]+\\s+\\w+$");
    private static final Pattern RankHeaderWithRank = Pattern.compile("^Rank\\n(\\d+)\\.$");
    private static final Pattern NameClubAndTime =
            Pattern.compile("^([\\w\\s-äöüßÄÖÜŚ\\.]+\\w)\\s{5,}" + "([\\w\\s" + "-äöüßÄÖÜŚ\\.]+[\\w\\.])\\s{5,}" + "(\\d:\\d{2}\\.\\d{2})$");
    private static final Pattern NameAndYB = Pattern.compile("^([\\w',\\.\\s-]*?)\\s{5,}(\\d+)$");
    private static final Pattern RankNameAndMembers = Pattern.compile("^(\\d+)\\.\\s+([\\w\\d\\s]+)\\n(.*)$");

    private OrangeCupResult fixEntry(OrangeCupResult l) {
        l.fixNullsAndTrim();
        clearHeaderRow(l);
        fixHeadersAndAgegroup(l);
        fixEvent1Row(l);
        fixRankHoldsGenderDisciplineAndAgegroup(l);
        fixRankHoldsEventGenderAndDiscipline(l);
        fixRankHoldsEventAndDateRow(l);
        fixYearOfBirthHoldsGenderAndDiscipline(l);
        fixYearOfBirthHoldsClub(l);
        fixTimeHoldsAgegroupAndResultsHeader(l);
        fixTimeHoldsAgegroup(l);
        fixResultRow(l);
        fixDiscipline(l);
        fixRank(l);
        fixNameHoldsNameClubAndTime(l);
        fixRankHoldsRankAndTimeHeader(l);
        fixRankHoldsRankAndYBHeader(l);
        fixNameHoldNameAndYB(l);
        fixNameLinebreak(l);
        fixRankHoldsRankNameAndMembers(l);
        fixRankHoldsEventGenderDisciplineAndAgegroup(l);
        fixClub2HoldsYearOfBirth(l);
        fixClub2HoldsGenderAndDiscipline(l);
        fixClubHoldsAgegroup(l);
        fixRankHoldsEvent(l);
        fixName(l);
        fixTime(l);
        fixMembers(l);
        fixClub(l);
        fixYearOfBirth(l);
        fixGender(l);
        return l;
    }

    private void fixRankHoldsEvent(OrangeCupResult l) {
        Matcher matcher = EventPattern.matcher(l.getRank());
        if (matcher.matches()) {
            l.setRank("");
            l.setEvent(matcher.group(1));
        }
    }

    private void fixTimeHoldsAgegroup(OrangeCupResult l) {
        if (l.getTime().endsWith("and older")) {
            l.setAgegroup(l.getTime());
            l.setTime("");
        }
        if (l.getTime2().endsWith("and older")) {
            l.setAgegroup(l.getTime2());
            l.setTime2("");
        }
        if (l.getTime3().endsWith("and older")) {
            l.setAgegroup(l.getTime3());
            l.setTime3("");
        }
    }

    private void fixClubHoldsAgegroup(OrangeCupResult l) {
        if (l.getClub().startsWith("Masters ")) {
            l.setAgegroup(l.getClub());
            l.setClub("");
        }
        if (l.getClub2().startsWith("Masters ")) {
            l.setAgegroup(l.getClub2());
            l.setClub2("");
        }
        if (l.getClub3().startsWith("Masters ")) {
            l.setAgegroup(l.getClub3());
            l.setClub3("");
        }
    }

    private void fixClub2HoldsGenderAndDiscipline(OrangeCupResult l) {
        Matcher matcher = GenderAndDiscipline.matcher(l.getClub());
        if (matcher.matches()) {
            l.setClub("");
            l.setGender(matcher.group(1));
            l.setDiscipline(matcher.group(2));
        }
        matcher = GenderAndDiscipline.matcher(l.getClub2());
        if (matcher.matches()) {
            l.setClub2("");
            l.setGender(matcher.group(1));
            l.setDiscipline(matcher.group(2));
        }
        matcher = GenderAndDiscipline.matcher(l.getClub3());
        if (matcher.matches()) {
            l.setClub3("");
            l.setGender(matcher.group(1));
            l.setDiscipline(matcher.group(2));
        }
    }

    private void fixClub2HoldsYearOfBirth(OrangeCupResult l) {
        if (l.getYearOfBirth().isBlank() && l.getClub2().length() == 2) {
            l.setYearOfBirth(l.getClub2());
            l.setClub2("");
        }
    }

    private void fixGender(OrangeCupResult l) {
        if (l.getGender().equalsIgnoreCase("boys")) {
            l.setGender("Men");
        }
    }

    private void fixYearOfBirthHoldsClub(OrangeCupResult l) {
        if (l.getYearOfBirth().length() > 2 && l.getClub().isBlank()) {
            l.setClub(l.getYearOfBirth());
            l.setYearOfBirth("");
        }
    }

    private void fixYearOfBirth(OrangeCupResult l) {
        if (l.getYearOfBirth().isBlank() && !l.getYearOfBirth2().isBlank()) {
            if (l.getYearOfBirth2().length() > 2 && l.getClub().isBlank()) {
                l.setClub(l.getYearOfBirth2());
            } else {
                l.setYearOfBirth(l.getYearOfBirth2());
            }
            l.setYearOfBirth2("");
        }
        if (l.getYearOfBirth().equalsIgnoreCase("YB")) {
            l.setYearOfBirth("");
        }
    }

    private void fixRankHoldsEventGenderDisciplineAndAgegroup(OrangeCupResult l) {
        Matcher matcher = EventGenderDisciplineAndAgegroupPattern.matcher(l.getRank());
        if (matcher.matches()) {
            l.setRank("");
            l.setEvent(matcher.group(1));
            l.setGender(matcher.group(2));
            l.setDiscipline(matcher.group(3));
            l.setAgegroup(matcher.group(4));
        }
    }

    private void fixRankHoldsEventGenderAndDiscipline(OrangeCupResult l) {
        Matcher matcher = EventGenderAndDisciplinePattern.matcher(l.getRank());
        if (matcher.matches()) {
            l.setRank("");
            l.setEvent(matcher.group(1));
            l.setGender(matcher.group(2));
            l.setDiscipline(matcher.group(3));
        }
    }

    private void fixNameLinebreak(OrangeCupResult l) {
        String[] parts = l.getName().split("\n");
        if (parts.length == 2) {
            l.setName(parts[0].trim());
            l.setMembers(parts[1].trim());
        }
    }

    private void fixNameHoldNameAndYB(OrangeCupResult l) {
        Matcher matcher = NameAndYB.matcher(l.getName());
        if (matcher.matches()) {
            l.setName(matcher.group(1));
            l.setYearOfBirth(matcher.group(2));
        }
    }

    private void fixRankHoldsRankAndYBHeader(OrangeCupResult l) {
        Matcher matcher = RankAndYBHeaders.matcher(l.getRank());
        if (matcher.matches()) {
            l.setRank("");
        }
    }

    private void fixRankHoldsRankNameAndMembers(OrangeCupResult l) {
        Matcher matcher = RankNameAndMembers.matcher(l.getRank());
        if (matcher.matches()) {
            l.setRank(matcher.group(1));
            l.setName(matcher.group(2));
            l.setMembers(matcher.group(3));
        }
    }

    private void fixRankHoldsRankAndTimeHeader(OrangeCupResult l) {
        Matcher matcher = RankAndTimeHeaders.matcher(l.getRank());
        if (matcher.matches()) {
            l.setRank("");
        }
    }

    private void fixNameHoldsNameClubAndTime(OrangeCupResult l) {
        Matcher matcher = NameClubAndTime.matcher(l.getName());
        if (matcher.matches()) {
            l.setName(matcher.group(1));
            l.setClub(matcher.group(2));
            l.setTime(matcher.group(3));
        }
    }

    private void fixRank(OrangeCupResult l) {
        Matcher matcher = RankHeaderWithRank.matcher(l.getRank());
        if (matcher.matches()) {
            l.setRank(matcher.group(1));
        }
        if (l.getRank().equals(",")) {
            l.setRank("");
        }
        if (l.getRank().endsWith(".")) {
            l.setRank(l.getRank().substring(0, l.getRank().length() - 1));
        }
        if (l.getRank().equalsIgnoreCase("Junioren")) {
            l.setAgegroup("Junioren");
            l.setRank("");
        }
        if (l.getRank().equalsIgnoreCase("Rank")) {
            l.setRank("");
        }
        l.setRank(l.getRank().trim());
    }

    private void fixName(OrangeCupResult l) {
        if (l.getName().startsWith("Event ") || l.getName().startsWith("World Record ") || l.getName().equalsIgnoreCase("YB")) {
            l.setName("");
        }
    }

    private void fixMembers(OrangeCupResult l) {
        if (l.getMembers().equalsIgnoreCase("Open")) {
            if (l.getAgegroup().isBlank()) {
                l.setAgegroup("Open");
            }
            l.setMembers("");
        }
        if (l.getMembers().matches("^\\d{2}-\\d{2}-\\d{4}$") || l.getMembers().contains("Record")) {
            l.setMembers("");
        }
    }

    private void fixClub(OrangeCupResult l) {
        if (l.getClub().isBlank() && !l.getClub2().isBlank()) {
            l.setClub(l.getClub2());
            l.setClub2("");
        }
        if (l.getClub().isBlank() && !l.getName2().isBlank()) {
            l.setClub(l.getName2());
            l.setName2("");
        }
        if (l.getClub().isBlank() && !l.getClub3().isBlank()) {
            l.setClub(l.getClub3());
            l.setClub3("");
        }
        if (l.getClub().equalsIgnoreCase("Time")) {
            l.setClub("");
        }
    }

    private void fixTime(OrangeCupResult l) {
        if (l.getTime().equalsIgnoreCase("Time")) {
            l.setTime("");
        }
        if (l.getTime().isBlank() && !l.getTime2().isBlank()) {
            l.setTime(l.getTime2());
            l.setTime2("");
        }
        if (l.getTime().isBlank() && !l.getTime3().isBlank() && !l.getTime3().startsWith("Open")) {
            l.setTime(l.getTime3());
            l.setTime3("");
        }
        if (l.getTime().equalsIgnoreCase("Time")) {
            l.setTime("");
        }
        if (l.getTime().equalsIgnoreCase("Results")) {
            l.setTime("");
        }
        l.setTime(l.getTime().replace('.', ','));
        if (!l.getTime().isBlank() && !l.getTime().contains(":")) {
            l.setTime("0:" + l.getTime());
        }
    }

    private void fixDiscipline(OrangeCupResult l) {
        Matcher matcher = DisciplinePlusRecord.matcher(l.getDiscipline());
        if (matcher.matches()) {
            l.setDiscipline(matcher.group(1));
        }
    }

    private void clearHeaderRow(OrangeCupResult l) {
        if (l.getRank().equals("Rank") && l.getYearOfBirth().equals("YB") && l.getTime().equals("Time")) {
            l.setRank("");
            l.setYearOfBirth("");
            l.setTime("");
        }
    }

    private void fixResultRow(OrangeCupResult l) {
        if (l.getName().isBlank()) {
            Matcher matcher = RankAndName.matcher(l.getRank());
            if (matcher.matches()) {
                l.setRank(matcher.group(1));
                l.setName(matcher.group(2));
            }
        }
        if (l.getRank().endsWith(",")) {
            l.setRank(l.getRank().replace(",", ""));
        }
        if (l.getName().isBlank()) {
            l.setName(l.getName2());
            l.setName2("");
        }
    }

    private void fixEvent1Row(OrangeCupResult l) {
        l.setRank(l.getRank().replace("\r\n", "\n").replace("\r", "\n"));
        Matcher matcher = EventGenderDisciplineAgegroupAndDatePattern.matcher(l.getRank());
        if (matcher.matches()) {
            l.setRank("");
            l.setEvent(matcher.group(1));
            l.setGender(matcher.group(2));
            l.setDiscipline(matcher.group(3));
            l.setAgegroup(matcher.group(4));
            l.setDate(matcher.group(5));
        }
    }

    private void fixRankHoldsGenderDisciplineAndAgegroup(OrangeCupResult l) {
        Matcher matcher = Event2Pattern.matcher(l.getRank());
        if (matcher.matches()) {
            l.setRank("");
            l.setEvent(matcher.group(1));
            l.setGender(matcher.group(2));
            l.setDiscipline(matcher.group(3));
            l.setAgegroup(matcher.group(4));
        }
    }

    private void fixYearOfBirthHoldsGenderAndDiscipline(OrangeCupResult l) {
        Matcher matcher = GenderAndDiscipline.matcher(l.getYearOfBirth());
        if (matcher.matches()) {
            l.setYearOfBirth("");
            l.setGender(matcher.group(1));
            l.setDiscipline(matcher.group(2));
        }
    }

    private void fixTimeHoldsAgegroupAndResultsHeader(OrangeCupResult l) {
        Matcher matcher = AgegroupAndResultsHeader.matcher(l.getTime());
        if (matcher.matches()) {
            l.setTime("");
            l.setAgegroup(matcher.group(1));
        }
    }

    private void fixRankHoldsEventAndDateRow(OrangeCupResult l) {
        Matcher matcher = EventAndDatePattern.matcher(l.getRank());
        if (matcher.matches()) {
            l.setRank("");
            l.setEvent(matcher.group(1));
            l.setDate(matcher.group(2));
        }
        matcher = EventAndDateAndWorldRecordPattern.matcher(l.getRank());
        if (matcher.matches()) {
            l.setRank("");
            l.setEvent(matcher.group(1));
            l.setDate(matcher.group(2));
        }
    }

    private void fixHeadersAndAgegroup(OrangeCupResult l) {
        l.setRank(l.getRank().replace("\r\n", "\n").replace("\r", "\n"));
        Matcher matcher = HeadersAndAgegroup.matcher(l.getRank());
        if (matcher.matches()) {
            l.setRank("");
            l.setAgegroup(matcher.group(1));
        }
        if (l.getRank().startsWith("Masters") || l.getRank().startsWith("Senioren")) {
            l.setAgegroup(l.getRank());
            l.setRank("");
        }
    }

    List<OrangeCupResult> read(Path path) throws IOException {
        try (Reader reader = new InputStreamReader(Files.newInputStream(path), StandardCharsets.UTF_8)) {
            CsvToBean<OrangeCupResult> cb = new CsvToBeanBuilder<OrangeCupResult>(reader).withType(OrangeCupResult.class).withSeparator(';').build();
            OrangeCupResult[] result = cb.parse().stream().map(this::fixEntry).toArray(OrangeCupResult[]::new);

            String event = result[0].getEvent();
            String agegroup = result[0].getAgegroup();
            String date = result[0].getDate().isBlank() ? "26-11-2022" : result[0].getDate();
            String discipline = result[0].getDiscipline();
            String gender = result[0].getGender();

            for (int x = 1; x < result.length; x++) {
                OrangeCupResult previous = result[x - 1];
                OrangeCupResult row = result[x];
                if (row.getRank().isBlank() && !row.getTime().isBlank() && previous.getTime().equals(row.getTime())) {
                    row.setRank(previous.getRank());
                }
                if (!row.getRank().matches("^\\d.*$") && !row.getRank().isBlank() && !row.getRank().matches("\\d+") && previous.getMembers().isBlank()) {
                    if (!row.getRank().equalsIgnoreCase("open")) {
                        previous.setMembers(row.getRank());
                    }
                    row.setRank("");
                    if (!row.isEmpty()) {
                        System.out.println(" X-> " + row);
                    }
                }
                if (row.getRank().isBlank() && row.getName().matches(".*?,.*?,.*") && previous.getMembers().isBlank() && previous.getYearOfBirth().isBlank()) {
                    previous.setMembers(row.getName());
                    row.setName("");
                }
                if (row.getRank().isBlank() && row.getName2().matches(".*?,.*?,.*") && previous.getMembers().isBlank() && previous.getYearOfBirth().isBlank()) {
                    previous.setMembers(row.getName2());
                    row.setName2("");
                }

                if (!row.getEvent().isBlank()) {
                    event = row.getEvent();
                }
                if (!row.getAgegroup().isBlank()) {
                    agegroup = row.getAgegroup();
                }
                if (!row.getDate().isBlank()) {
                    date = row.getDate();
                }
                if (!row.getDiscipline().isBlank()) {
                    discipline = row.getDiscipline();
                }
                if (!row.getGender().isBlank()) {
                    gender = row.getGender();
                }
                row.setEvent(event);
                row.setAgegroup(agegroup);
                row.setDate(date);
                row.setDiscipline(discipline);
                row.setGender(gender);
            }
            return Arrays.asList(result);
        }
    }
}
