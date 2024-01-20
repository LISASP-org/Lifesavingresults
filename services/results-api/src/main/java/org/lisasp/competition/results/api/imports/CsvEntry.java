package org.lisasp.competition.results.api.imports;

public record CsvEntry(String rank, String name, String club, String members, String time, String agegroup,
                       String gender, String discipline, String date, String event) {}
