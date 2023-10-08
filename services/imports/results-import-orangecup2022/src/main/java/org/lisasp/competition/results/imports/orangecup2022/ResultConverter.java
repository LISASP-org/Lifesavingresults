package org.lisasp.competition.results.imports.orangecup2022;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

class ResultConverter {
    void convertSaturday() throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String day = "Saturday";
        Path path = Path.of("data", "Results-" + day + ".csv");
        List<OrangeCupResult> data = new SaturdayResultReader().read(path);
        write(day, data);
    }

    void convertSunday() throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String day = "Sunday";
        Path path = Path.of("data", "Results-" + day + ".csv");
        List<OrangeCupResult> data = new SaturdayResultReader().read(path);
        write(day, data);
    }

    private void write(String day, List<OrangeCupResult> data) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        new ResultWriter().write(data.stream(), Path.of("data", "Valid-Results-" + day + ".csv"));
        new ResultWriter().writeInvalid(data.stream(), Path.of("data", "Invalid-Results-" + day + ".csv"));
        new ResultWriter().writeOther(data.stream(), Path.of("data", "Other-Results-" + day + ".csv"));
    }
}
