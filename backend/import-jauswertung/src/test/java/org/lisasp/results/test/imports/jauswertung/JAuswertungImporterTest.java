package org.lisasp.results.test.imports.jauswertung;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lisasp.results.api.*;
import org.lisasp.results.api.type.EventTypes;
import org.lisasp.results.api.type.Genders;
import org.lisasp.results.api.type.InputValueTypes;
import org.lisasp.results.api.type.PenaltyType;
import org.lisasp.results.imports.jauswertung.JAuswertungImporter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class JAuswertungImporterTest {

    private String individualNoEvent;
    private String individualNoEntry;
    private String individualSingleEntry;
    private String individualTwoEntries;
    private String individualTwoEvents;
    private JAuswertungImporter importer;

    @BeforeEach
    void prepare() throws URISyntaxException, IOException {
        importer = new JAuswertungImporter();

        individualNoEvent = readResource("/individual-no-event.json");
        individualNoEntry = readResource("/individual-no-entry.json");
        individualSingleEntry = readResource("/individual-single-entry.json");
        individualTwoEntries = readResource("/individual-two-entries.json");
        individualTwoEvents = readResource("/individual-two-events.json");
    }

    private String readResource(String filename) throws IOException, URISyntaxException {
        URL resource = getClass().getResource(filename);
        if (resource == null) {
            throw new FileNotFoundException("Could not find resource " + filename);
        }
        return Files.readString(Paths.get(resource.toURI()), StandardCharsets.UTF_8);
    }

    @Test
    void importCompetitionWithOneEventAndOneEntry() throws Exception {
        Competition competition = importer.importJson(individualSingleEntry);

        Event[] events = competition.getEvents();
        assertNotNull(events);
        assertEquals(1, events.length);
        Event event = events[0];

        Entry[] entries = event.getEntries();
        assertEquals(1, entries.length);

        Entry entry = entries[0];
        assertEquals("11", entry.getNumber());
        assertEquals("von Scleinitz, Centa", entry.getName());
        assertEquals("Neumarkt (ND)", entry.getClub());
        assertEquals("", entry.getNationality());
        assertEquals(0, entry.getPlaceInHeat());
        assertEquals(78530, entry.getTimeInMillis());
        assertEquals(new Start("1", (byte) 6), entry.getStart());
        assertEquals(1, entry.getPenalties().length);
        assertEquals(new Penalty("P321", PenaltyType.Points, (short) 123), entry.getPenalties()[0]);
    }

    @Test
    void importOneCompetitionWithOneEventAndNoEntry() throws Exception {
        Competition competition = importer.importJson(individualNoEntry);

        Event[] events = competition.getEvents();
        assertNotNull(events);
        assertEquals(1, events.length);
        Event event = events[0];

        assertNull(event.getId());
        assertEquals(EventTypes.INDIVIDUAL, event.getEventType());
        assertEquals(new Round((byte) 0, true), event.getRound());
        assertEquals("AK 12", event.getAgegroup());
        assertEquals(Genders.FEMALE, event.getGender());
        assertEquals(InputValueTypes.Time, event.getInputValueType());
    }

    @Test
    void importOneCompetitionWithNoEventAndNoEntry() throws Exception {
        Competition competition = importer.importJson(individualNoEvent);

        Event[] events = competition.getEvents();
        assertNotNull(events);
        assertEquals(0, events.length);
    }

    @Test
    void importCompetitionWithOneEventAndTwoEntries() throws Exception {

        Competition competition = importer.importJson(individualTwoEntries);

        Event[] events = competition.getEvents();
        assertNotNull(events);
        assertEquals(1, events.length);
        Event event = events[0];

        Entry[] entries = event.getEntries();

        Entry entry1 = entries[0];
        assertEquals("11", entry1.getNumber());
        assertEquals("von Scleinitz, Centa", entry1.getName());
        assertEquals("Neumarkt (ND)", entry1.getClub());
        assertEquals("", entry1.getNationality());
        assertEquals(0, entry1.getPlaceInHeat());
        assertEquals(78530, entry1.getTimeInMillis());
        assertEquals(new Start("1", (byte) 6), entry1.getStart());
        assertEquals(1, entry1.getPenalties().length);
        assertEquals(new Penalty("P321", PenaltyType.Points, (short) 123), entry1.getPenalties()[0]);

        Entry entry2 = entries[1];
        assertEquals("1", entry2.getNumber());
        assertEquals("Hain, Cosima", entry2.getName());
        assertEquals("Murrhardt (ND)", entry2.getClub());
        assertEquals("", entry2.getNationality());
        assertEquals(0, entry2.getPlaceInHeat());
        assertEquals(54720, entry2.getTimeInMillis());
        assertEquals(new Start("2", (byte) 1), entry2.getStart());
        assertEquals(0, entry2.getPenalties().length);
    }

    @Test
    void importCompetitionWithTwoEventAndTwoEntries() throws Exception {
        Competition competition = importer.importJson(individualTwoEvents);

        Event[] events = competition.getEvents();
        assertNotNull(events);
        assertEquals(2, events.length);
        Event event1 = events[0];
        Event event2 = events[1];

        Entry[] entries1 = event1.getEntries();
        assertEquals(1, entries1.length);

        Entry entry1 = entries1[0];
        assertEquals("11", entry1.getNumber());
        assertEquals("von Scleinitz, Centa", entry1.getName());
        assertEquals("Neumarkt (ND)", entry1.getClub());
        assertEquals("", entry1.getNationality());
        assertEquals(0, entry1.getPlaceInHeat());
        assertEquals(78530, entry1.getTimeInMillis());
        assertEquals(new Start("1", (byte) 6), entry1.getStart());
        assertEquals(0, entry1.getPenalties().length);

        Entry[] entries2 = event2.getEntries();
        assertEquals(1, entries2.length);

        Entry entry2 = entries2[0];
        assertEquals("44", entry2.getNumber());
        assertEquals("Winand, Constantin", entry2.getName());
        assertEquals("Altlandsberg (SH)", entry2.getClub());
        assertEquals("", entry2.getNationality());
        assertEquals(0, entry2.getPlaceInHeat());
        assertEquals(63120, entry2.getTimeInMillis());
        assertEquals(new Start("12", (byte) 5), entry2.getStart());
        assertEquals(1, entry2.getPenalties().length);
        assertEquals(new Penalty("W7", PenaltyType.Points, (short) 200), entry2.getPenalties()[0]);
    }
}
