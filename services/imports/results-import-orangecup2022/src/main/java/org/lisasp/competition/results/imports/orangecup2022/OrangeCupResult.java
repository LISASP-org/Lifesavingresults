package org.lisasp.competition.results.imports.orangecup2022;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;

import java.util.regex.Pattern;

@Data
public class OrangeCupResult {

    private static final Pattern TimePattern = Pattern.compile("^(\\d\\:)?\\d{2},\\d{2}$");

    @CsvBindByPosition(position = 0)
    private String rank;
    @CsvBindByPosition(position = 1)
    private String name;
    @CsvBindByPosition(position = 2)
    private String name2;
    @CsvBindByPosition(position = 3)
    private String yearOfBirth2;
    @CsvBindByPosition(position = 4)
    private String yearOfBirth;
    @CsvBindByPosition(position = 5)
    private String club2;
    @CsvBindByPosition(position = 6)
    private String club;
    @CsvBindByPosition(position = 7)
    private String club3;
    @CsvBindByPosition(position = 9)
    private String time2;
    @CsvBindByPosition(position = 10)
    private String time;
    @CsvBindByPosition(position = 11)
    private String time3;
    private String agegroup;
    private String gender;
    private String discipline;
    private String date;
    private String members;
    private String event;

    public void fixNullsAndTrim() {
        event = fixNullAndTrim(event);
        rank = fixNullAndTrim(rank);
        name = fixNullAndTrim(name);
        name2 = fixNullAndTrim(name2);
        yearOfBirth = fixNullAndTrim(yearOfBirth);
        yearOfBirth2 = fixNullAndTrim(yearOfBirth2);
        club = fixNullAndTrim(club);
        club2 = fixNullAndTrim(club2);
        club3 = fixNullAndTrim(club3);
        time = fixNullAndTrim(time);
        time2 = fixNullAndTrim(time2);
        time3 = fixNullAndTrim(time3);
        agegroup = fixNullAndTrim(agegroup);
        gender = fixNullAndTrim(gender);
        discipline = fixNullAndTrim(discipline);
        date = fixNullAndTrim(date);
        members = fixNullAndTrim(members);
    }

    OrangeCupEntry toEntry() {
        return OrangeCupEntry.builder()
                             .rank(rank)
                             .name(name)
                             .yearOfBirth(yearOfBirth)
                             .club(club)
                             .time(time)
                             .agegroup(agegroup)
                             .gender(gender)
                             .discipline(discipline)
                             .date(date)
                             .members(members)
                             .event(event)
                             .build();
    }

    private String fixNullAndTrim(String value) {
        if (value == null) {
            return "";
        }
        return value.trim();
    }

    public boolean isEmpty() {
        return isEmpty(rank) && isEmpty(name) && isEmpty(yearOfBirth) && isEmpty(club) && isEmpty(time) && isEmpty(agegroup) && isEmpty(gender) &&
               isEmpty(discipline) && isEmpty(date) && isEmpty(members);
    }

    private boolean isEmpty(String value) {
        return value.isBlank();
    }

    public boolean isValid() {
        if (!isEntry()) {
            return false;
        }
        if (!name2.isBlank()) {
            return false;
        }
        if (containsLineBreak()) {
            return false;
        }
        if (!rank.matches("\\d+")) {
            return false;
        }
        if (name.isBlank()) {
            return false;
        }
        if (club.isBlank()) {
            return false;
        }
        if (!yearOfBirth.isBlank() && !members.isBlank()) {
            return false;
        }
        if (yearOfBirth.length() != 2 && members.isBlank()) {
            return false;
        }
        if (!members.isBlank() && !members.contains(",")) {
            return false;
        }
        if (time.isBlank()) {
            return false;
        }
        if (!TimePattern.matcher(time).matches()) {
            return false;
        }
        return true;
    }

    private boolean containsLineBreak() {
        return containsLineBreak(rank) || containsLineBreak(name) || containsLineBreak(yearOfBirth) || containsLineBreak(club) || containsLineBreak(time) ||
               containsLineBreak(agegroup) || containsLineBreak(gender) || containsLineBreak(discipline) || containsLineBreak(date) ||
               containsLineBreak(members);
    }

    private boolean containsLineBreak(String value) {
        return value.contains("\n");
    }

    public boolean isEntry() {
        return !isEmpty(rank) || !isEmpty(name) || !isEmpty(yearOfBirth) || !isEmpty(club) || !isEmpty(time) || !isEmpty(members);
    }
}
