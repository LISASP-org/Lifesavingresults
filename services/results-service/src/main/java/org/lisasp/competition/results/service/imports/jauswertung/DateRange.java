package org.lisasp.competition.results.service.imports.jauswertung;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record DateRange(LocalDate first, LocalDate last) {

    private static final Pattern RANGE1 = Pattern.compile("^(\\d{2})-(\\d{2})\\.(\\d{2}\\.\\d{4})$");
    private static final Pattern RANGE2 = Pattern.compile("^(\\d{2}\\.\\d{2})\\.-(\\d{2}\\.\\d{2})\\.(\\d{4})$");


    public static DateRange fromString(String date) {
        if (date == null || date.isBlank()) {
            return new DateRange(null, null);
        }
        LocalDate value = toDate(date);
        if (value != null) {
            return new DateRange(value, value);
        }
        Matcher matcher1 = RANGE1.matcher(date);
        if (matcher1.matches()) {
            String day1 = matcher1.group(1);
            String day2 = matcher1.group(2);
            String monthAndYear = matcher1.group(3);

            LocalDate from = toDate(String.format("%s.%s", day1, monthAndYear));
            LocalDate till = toDate(String.format("%s.%s", day2, monthAndYear));
            if (from != null && till != null) {
                return new DateRange(from, till);
            }
            return new DateRange(null, null);
        }
        Matcher matcher2 = RANGE2.matcher(date);
        if (matcher2.matches()) {
            String dayAndMonth1 = matcher2.group(1);
            String dayAndMonth2 = matcher2.group(2);
            String year = matcher2.group(3);

            LocalDate from = toDate(String.format("%s.%s", dayAndMonth1, year));
            LocalDate till = toDate(String.format("%s.%s", dayAndMonth2, year));
            if (from != null && till != null) {
                return new DateRange(from, till);
            }
            return new DateRange(null, null);
        }
        return new DateRange(null, null);
    }

    private static LocalDate toDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException ex) {
            // Nothing to do;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException ex) {
            // Nothing to do;
        }
        return null;
    }
}
