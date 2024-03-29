package org.lisasp.competition.results.imports.em2023;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class PrelimTests {

    @Test
    void parseOneEntry() throws IOException {
        Entry[] expected = {new Entry("OPEN", "Women", "Second, First", "NO/W", 35190, "50m Manikin Carry", 1, "", "Prelim", "10.09.2023", "25", "03")};
        List<String> lines = Files.readAllLines(Path.of("src", "test", "resources", "Individual-ManikinCarry-Women-Prelim.txt"));
        ResultParser parser = new ResultParser();

        parser.push(lines);

        Entry[] actual = parser.read();

        assertArrayEquals(expected, actual);
    }

    @Test
    void parse() throws IOException {
        Entry[] expected = {
                new Entry("OPEN", "Men", "Club A", "Club A", 10270, "Line Throw", 1, "", "Finals", "23.09.2023", "14", ""),
                new Entry("OPEN", "Men", "Club B", "Club B", 12560, "Line Throw", 2, "", "Finals", "23.09.2023", "14", ""),
                new Entry("OPEN", "Men", "Club C", "Club C", 13350, "Line Throw", 3, "", "Finals", "23.09.2023", "14", ""),
                new Entry("OPEN", "Men", "Club D", "Club D", 13490, "Line Throw", 4, "", "Finals", "23.09.2023", "14", ""),
                new Entry("OPEN", "Men", "Club E", "Club E", 14870, "Line Throw", 5, "", "Finals", "23.09.2023", "14", ""),
                new Entry("OPEN", "Men", "Club F", "Club F", 15580, "Line Throw", 6, "", "Finals", "23.09.2023", "14", ""),
                new Entry("OPEN", "Men", "Club G", "Club G", 28030, "Line Throw", 7, "", "Finals", "23.09.2023", "14", ""),
                new Entry("OPEN", "Men", "Club H", "Club H", 0, "Line Throw", 8, "OTL", "Finals", "23.09.2023", "14", ""),
                new Entry("OPEN", "Men", "Club I", "Club I", 23660, "Line Throw", 9, "", "Finals", "23.09.2023", "14", ""),
        };
        List<String> lines = Files.readAllLines(Path.of("src", "test", "resources", "Team-LineThrow-Men-Final.txt"));
        ResultParser parser = new ResultParser();

        parser.push(lines);

        Entry[] actual = parser.read();

        assertArrayEquals(expected, actual);
    }
}
