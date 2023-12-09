package org.lisasp.competition.results.imports.em2023;

import java.time.LocalDate;

public record Entry(String agegroup, String gender, String name, String club, int timeInMillis, String discipline,
                    int rank, String penalty, String round, String date, String event, String yearOfBirth) {
    public boolean isEntry() {
        return true;
    }

    public boolean isValid() {
        return true;
    }

    public EMEntity toEntry() {
        return EMEntity.builder()
                       .agegroup(agegroup)
                       .club(club)
                       .date(date)
                       .discipline(discipline)
                       .event(event)
                       .gender(gender)
                       .members("")
                       .name(name)
                       .rank("" + rank)
                       .time(formatMillis(timeInMillis))
                       .yearOfBirth(yearOfBirth)
                       .build();
    }

    private static String formatMillis(int timeInMillis) {
        int minute = timeInMillis / 60 / 1000;
        int seconds = (timeInMillis / 1000) % 60;
        int millis = timeInMillis % 1000;
        return String.format("%d:%02d.%03d", minute, seconds, millis);
    }
}
