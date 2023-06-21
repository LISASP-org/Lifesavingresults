package org.lisasp.competition.results.api.imports;

import com.opencsv.bean.CsvBindByName;

public record CsvEntry(@CsvBindByName String rank,
                       @CsvBindByName String name,
                       @CsvBindByName String club,
                       @CsvBindByName String members,
                       @CsvBindByName String time,
                       @CsvBindByName String agegroup,
                       @CsvBindByName String gender,
                       @CsvBindByName String discipline,
                       @CsvBindByName String date,
                       @CsvBindByName String event) { }
