package org.lisasp.competition.results.imports.em2023;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class FinalTests {

    @Test
    void parseOneEntry() throws IOException {
        Entry[] expected = {new Entry("OPEN", "Men", "Name", "Club", 10270, "Line Throw", 1, "", "Finals")};
        List<String> lines = Files.readAllLines(Path.of("src", "test", "resources", "Team-LineThrow-Men-Final-One-Entry.txt"));
        ResultParser parser = new ResultParser();

        parser.push(lines);

        Entry[] actual = parser.read();

        assertArrayEquals(expected, actual);
    }

    @Test
    void parse() throws IOException {
        Entry[] expected = {
                new Entry("OPEN", "Men", "Club A", "Club A", 10270, "Line Throw", 1, "", "Finals"),
                new Entry("OPEN", "Men", "Club B", "Club B", 12560, "Line Throw", 2, "", "Finals"),
                new Entry("OPEN", "Men", "Club C", "Club C", 13350, "Line Throw", 3, "", "Finals"),
                new Entry("OPEN", "Men", "Club D", "Club D", 13490, "Line Throw", 4, "", "Finals"),
                new Entry("OPEN", "Men", "Club E", "Club E", 14870, "Line Throw", 5, "", "Finals"),
                new Entry("OPEN", "Men", "Club F", "Club F", 15580, "Line Throw", 6, "", "Finals"),
                new Entry("OPEN", "Men", "Club G", "Club G", 28030, "Line Throw", 7, "", "Finals"),
                new Entry("OPEN", "Men", "Club H", "Club H", 0, "Line Throw", 8, "OTL", "Finals"),
                new Entry("OPEN", "Men", "Club I", "Club I", 23660, "Line Throw", 9, "", "Finals"),
        };
        List<String> lines = Files.readAllLines(Path.of("src", "test", "resources", "Team-LineThrow-Men-Final.txt"));
        ResultParser parser = new ResultParser();

        parser.push(lines);

        Entry[] actual = parser.read();

        assertArrayEquals(expected, actual);
    }
}
