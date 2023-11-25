package org.lisasp.competition.results.imports.em2023;

import com.opencsv.bean.CsvBindByName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ResultWriter.CsvBindByNameOrder({ "rank", "name", "yearOfBirth", "club", "members", "time", "agegroup", "gender", "discipline", "date", "event" })
public class EMEntity {
    @CsvBindByName
    private String rank;
    @CsvBindByName
    private String name;
    @CsvBindByName
    private String yearOfBirth;
    @CsvBindByName
    private String club;
    @CsvBindByName
    private String members;
    @CsvBindByName
    private String time;
    @CsvBindByName
    private String agegroup;
    @CsvBindByName
    private String gender;
    @CsvBindByName
    private String discipline;
    @CsvBindByName
    private String date;
    @CsvBindByName
    private String event;
}
