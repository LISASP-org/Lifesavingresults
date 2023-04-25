package org.lisasp.competition.results.imports.orangecup2022;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SaturdayResultReaderTests {

    @Test
    void eventLine1() throws IOException {
        List<OrangeCupResult> results = new SaturdayResultReader().read(Path.of("src", "test", "resources", "EventLine1.csv"));

        assertEquals(1, results.size());

        OrangeCupResult actual = results.get(0);
        assertEquals("", actual.getRank());
        assertEquals("Women", actual.getGender());
        assertEquals("200m Obstacle Swim", actual.getDiscipline());
        assertEquals("Open", actual.getAgegroup());
        assertEquals("26-11-2022", actual.getDate());

        assertEquals("", actual.getClub());
        assertEquals("", actual.getName());
        assertEquals("", actual.getTime());
        assertEquals("", actual.getYearOfBirth());
    }

    @Test
    void eventLine2() throws IOException {
        List<OrangeCupResult> results = new SaturdayResultReader().read(Path.of("src", "test", "resources", "EventLine2.csv"));

        assertEquals(1, results.size());

        OrangeCupResult actual = results.get(0);
        assertEquals("", actual.getRank());
        assertEquals("Women", actual.getGender());
        assertEquals("200m Obstacle Swim", actual.getDiscipline());
        assertEquals("Junioren", actual.getAgegroup());
        assertEquals("", actual.getDate());

        assertEquals("", actual.getClub());
        assertEquals("", actual.getName());
        assertEquals("", actual.getTime());
        assertEquals("", actual.getYearOfBirth());
    }

    @Test
    void eventLine3() throws IOException {
        List<OrangeCupResult> results = new SaturdayResultReader().read(Path.of("src", "test", "resources", "EventLine3.csv"));

        assertEquals(1, results.size());

        OrangeCupResult actual = results.get(0);
        assertEquals("", actual.getRank());
        assertEquals("Men", actual.getGender());
        assertEquals("200m Obstacle Swim", actual.getDiscipline());
        assertEquals("Open", actual.getAgegroup());
        assertEquals("26-11-2022", actual.getDate());

        assertEquals("", actual.getClub());
        assertEquals("", actual.getName());
        assertEquals("", actual.getTime());
        assertEquals("", actual.getYearOfBirth());
    }

    @Test
    void headersAndAgegroup() throws IOException {
        List<OrangeCupResult> results = new SaturdayResultReader().read(Path.of("src", "test", "resources",
                                                                                "HeadersAndAgegroupLine.csv"));

        assertEquals(1, results.size());

        OrangeCupResult actual = results.get(0);
        assertEquals("", actual.getRank());
        assertEquals("", actual.getGender());
        assertEquals("", actual.getDiscipline());
        assertEquals("Junioren", actual.getAgegroup());
        assertEquals("", actual.getDate());

        assertEquals("", actual.getClub());
        assertEquals("", actual.getName());
        assertEquals("", actual.getTime());
        assertEquals("", actual.getYearOfBirth());
    }

    @Test
    void resultIndividualLine1() throws IOException {
        List<OrangeCupResult> results = new SaturdayResultReader().read(Path.of("src", "test", "resources",
                                                                                "ResultIndividualLine1.csv"));

        assertEquals(1, results.size());

        OrangeCupResult actual = results.get(0);
        assertEquals("1", actual.getRank());
        assertEquals("", actual.getGender());
        assertEquals("", actual.getDiscipline());
        assertEquals("", actual.getAgegroup());
        assertEquals("", actual.getDate());

        assertEquals("Team Example", actual.getClub());
        assertEquals("LASTNAME, Firstname", actual.getName());
        assertEquals("2:34.56", actual.getTime());
        assertEquals("05", actual.getYearOfBirth());
    }

    @Test
    void resultIndividualLine2() throws IOException {
        List<OrangeCupResult> results = new SaturdayResultReader().read(Path.of("src", "test", "resources",
                                                                                "ResultIndividualLine2.csv"));

        assertEquals(1, results.size());

        OrangeCupResult actual = results.get(0);
        assertEquals("1", actual.getRank());
        assertEquals("", actual.getGender());
        assertEquals("", actual.getDiscipline());
        assertEquals("", actual.getAgegroup());
        assertEquals("", actual.getDate());

        assertEquals("Team Example", actual.getClub());
        assertEquals("LASTNAME, Firstname", actual.getName());
        assertEquals("2:34.56", actual.getTime());
        assertEquals("05", actual.getYearOfBirth());
    }

    @Test
    void resultIndividualLine3() throws IOException {
        List<OrangeCupResult> results = new SaturdayResultReader().read(Path.of("src", "test", "resources",
                                                                                "ResultIndividualLine3.csv"));

        assertEquals(1, results.size());

        OrangeCupResult actual = results.get(0);
        assertEquals("10", actual.getRank());
        assertEquals("", actual.getGender());
        assertEquals("", actual.getDiscipline());
        assertEquals("", actual.getAgegroup());
        assertEquals("", actual.getDate());

        assertEquals("Team Example", actual.getClub());
        assertEquals("LASTNAME, Firstname", actual.getName());
        assertEquals("2:34.56", actual.getTime());
        assertEquals("05", actual.getYearOfBirth());
    }

    @Test
    void headerLine1() throws IOException {
        List<OrangeCupResult> results = new SaturdayResultReader().read(Path.of("src", "test", "resources", "HeaderLine1.csv"));

        assertEquals(1, results.size());

        OrangeCupResult actual = results.get(0);
        assertEquals("", actual.getRank());
        assertEquals("", actual.getGender());
        assertEquals("", actual.getDiscipline());
        assertEquals("", actual.getAgegroup());
        assertEquals("", actual.getDate());

        assertEquals("", actual.getClub());
        assertEquals("", actual.getName());
        assertEquals("", actual.getTime());
        assertEquals("", actual.getYearOfBirth());
    }

    @Test
    void headerLine2() throws IOException {
        List<OrangeCupResult> results = new SaturdayResultReader().read(Path.of("src", "test", "resources", "HeaderLine2.csv"));

        assertEquals(1, results.size());

        OrangeCupResult actual = results.get(0);
        assertEquals("", actual.getRank());
        assertEquals("", actual.getGender());
        assertEquals("", actual.getDiscipline());
        assertEquals("", actual.getAgegroup());
        assertEquals("", actual.getDate());

        assertEquals("", actual.getClub());
        assertEquals("", actual.getName());
        assertEquals("", actual.getTime());
        assertEquals("", actual.getYearOfBirth());
    }

    @Test
    void agegroup1() throws IOException {
        List<OrangeCupResult> results = new SaturdayResultReader().read(Path.of("src", "test", "resources", "Agegroup1.csv"));

        assertEquals(1, results.size());

        OrangeCupResult actual = results.get(0);
        assertEquals("", actual.getRank());
        assertEquals("", actual.getGender());
        assertEquals("", actual.getDiscipline());
        assertEquals("Masters 30 - 39", actual.getAgegroup());
        assertEquals("", actual.getDate());

        assertEquals("", actual.getClub());
        assertEquals("", actual.getName());
        assertEquals("", actual.getTime());
        assertEquals("", actual.getYearOfBirth());
    }

    @Test
    void agegroup2() throws IOException {
        List<OrangeCupResult> results = new SaturdayResultReader().read(Path.of("src", "test", "resources", "Agegroup2.csv"));

        assertEquals(1, results.size());

        OrangeCupResult actual = results.get(0);
        assertEquals("", actual.getRank());
        assertEquals("", actual.getGender());
        assertEquals("", actual.getDiscipline());
        assertEquals("Masters 40 - 49", actual.getAgegroup());
        assertEquals("", actual.getDate());

        assertEquals("", actual.getClub());
        assertEquals("", actual.getName());
        assertEquals("", actual.getTime());
        assertEquals("", actual.getYearOfBirth());
    }

    @Test
    void agegroup3() throws IOException {
        List<OrangeCupResult> results = new SaturdayResultReader().read(Path.of("src", "test", "resources", "Agegroup3.csv"));

        assertEquals(1, results.size());

        OrangeCupResult actual = results.get(0);
        assertEquals("", actual.getRank());
        assertEquals("", actual.getGender());
        assertEquals("", actual.getDiscipline());
        assertEquals("Senioren", actual.getAgegroup());
        assertEquals("", actual.getDate());

        assertEquals("", actual.getClub());
        assertEquals("", actual.getName());
        assertEquals("", actual.getTime());
        assertEquals("", actual.getYearOfBirth());
    }

    @Test
    void resultTeamLine1() throws IOException {
        List<OrangeCupResult> results = new SaturdayResultReader().read(Path.of("src", "test", "resources",
                                                                                "ResultTeamLine1.csv"));

        assertEquals(1, results.size());

        OrangeCupResult actual = results.get(0);
        assertEquals("2", actual.getRank());
        assertEquals("", actual.getGender());
        assertEquals("", actual.getDiscipline());
        assertEquals("", actual.getAgegroup());
        assertEquals("", actual.getDate());

        assertEquals("Team Example", actual.getClub());
        assertEquals("Team Example 1", actual.getName());
        assertEquals("1:56.57", actual.getTime());
        assertEquals("", actual.getYearOfBirth());
    }
}
