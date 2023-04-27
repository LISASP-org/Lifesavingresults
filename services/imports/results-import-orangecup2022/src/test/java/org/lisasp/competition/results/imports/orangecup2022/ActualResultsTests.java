package org.lisasp.competition.results.imports.orangecup2022;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class ActualResultsTests {

    @Test
    void testSaturdayResults() throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        if (!Files.exists(Path.of("data", "Results-Saturday.csv"))) {
            return;
        }

        new ResultConverter().convertSaturday();

        assertFileEquals(Path.of("data", "expected", "Invalid-Results-Saturday.csv"), Path.of("data", "Invalid-Results-Saturday.csv"));
        assertFileEquals(Path.of("data", "expected", "Other-Results-Saturday.csv"), Path.of("data", "Other-Results-Saturday.csv"));
        assertFileEquals(Path.of("data", "expected", "Valid-Results-Saturday.csv"), Path.of("data", "Valid-Results-Saturday.csv"));
    }

    @Test
    void testSundayResults() throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        if (!Files.exists(Path.of("data", "Results-Sunday.csv"))) {
            return;
        }

        new ResultConverter().convertSunday();

        assertFileEquals(Path.of("data", "expected", "Invalid-Results-Sunday.csv"), Path.of("data", "Invalid-Results-Sunday.csv"));
        assertFileEquals(Path.of("data", "expected", "Other-Results-Sunday.csv"), Path.of("data", "Other-Results-Sunday.csv"));
        assertFileEquals(Path.of("data", "expected", "Valid-Results-Sunday.csv"), Path.of("data", "Valid-Results-Sunday.csv"));
    }

    private static void assertFileEquals(Path expectedPath, Path actualPath) throws IOException {
        byte[] expected = Files.readAllBytes(expectedPath);
        byte[] actual = Files.readAllBytes(actualPath);

        assertArrayEquals(expected, actual);
    }
}
