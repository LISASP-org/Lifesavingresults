package org.lisasp.competition.results.imports.orangecup2022;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {


        Path path = Path.of("data", "Results-Saturday.csv");
        List<OrangeCupResult> data = new SaturdayResultReader().read(path);
        new ResultWriter().write(data.stream(), Path.of("data", "Valid-Results-Saturday.csv"));
        new ResultWriter().writeInvalid(data.stream(), Path.of("data", "Invalid-Results-Saturday.csv"));
        new ResultWriter().writeOther(data.stream(), Path.of("data", "Other-Results-Saturday.csv"));


        // Path path = Path.of("data", "Results-Sunday.csv");
        // new SundayResultReader().read(path).stream().filter(e -> !e.isEmpty()).forEach(l -> System.out.println(l));
    }

}
