package org.lisasp.competition.results.imports.rescue2022;

import com.opencsv.bean.CsvBindByName;
import lombok.Builder;
import lombok.Data;
import org.lisasp.competition.results.imports.rescue2022.csv.CsvBindByNameOrder;

@Data
@Builder
@CsvBindByNameOrder({"rank", "name", "yearOfBirth", "club", "members", "time", "agegroup", "gender", "discipline", "date", "event" })
public class RescueEntity {
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
