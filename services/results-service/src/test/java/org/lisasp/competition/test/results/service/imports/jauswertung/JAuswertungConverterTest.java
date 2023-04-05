package org.lisasp.competition.test.results.service.imports.jauswertung;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lisasp.competition.base.api.type.*;
import org.lisasp.competition.results.api.imports.Competition;
import org.lisasp.competition.results.api.imports.Entry;
import org.lisasp.competition.results.api.imports.Event;
import org.lisasp.competition.results.api.value.Penalty;
import org.lisasp.competition.results.api.value.Round;
import org.lisasp.competition.results.api.value.Start;
import org.lisasp.competition.results.api.value.Swimmer;
import org.lisasp.competition.results.service.imports.jauswertung.JAuswertungConverter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JAuswertungConverterTest {

    private String individualNoEvent;
    private String individualNoEntry;
    private String individualSingleEntry;
    private String individualTwoEntries;
    private String individualTwoEvents;
    private JAuswertungConverter importer;

    @BeforeEach
    void prepare() throws URISyntaxException, IOException {
        importer = new JAuswertungConverter();

        individualNoEvent = readResource("/jauswertung/individual-no-event.json");
        individualNoEntry = readResource("/jauswertung/individual-no-entry.json");
        individualSingleEntry = readResource("/jauswertung/individual-single-entry.json");
        individualTwoEntries = readResource("/jauswertung/individual-two-entries.json");
        individualTwoEvents = readResource("/jauswertung/individual-two-events.json");
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

        Event[] events = competition.events();
        assertNotNull(events);
        assertEquals(1, events.length);
        Event event = events[0];
        assertEquals(new Round((byte)0, RoundType.Final), event.round());

        Entry[] entries = event.entries();
        assertEquals(1, entries.length);

        Entry entry = entries[0];
        assertEquals("11", entry.number());
        assertEquals("von Domas, Gerta", entry.name());
        assertEquals("Neumarkt (ND)", entry.club());
        assertEquals("", entry.nationality());
        assertEquals(0, entry.placeInHeat());
        assertEquals(78530, entry.timeInMillis());
        assertEquals(new Start("1", (byte) 6), entry.start());
        assertEquals(1, entry.penalties().length);
        assertEquals(new Penalty("P321", PenaltyType.Points, (short) 123), entry.penalties()[0]);
        assertEquals(1, entry.swimmer().length);
        assertEquals(new Swimmer("11", "Gerta", "von Domas", Sex.Female, (short)2013), entry.swimmer()[0]);
    }

    @Test
    void importOneCompetitionWithOneEventAndNoEntry() throws Exception {
        Competition competition = importer.importJson(individualNoEntry);

        Event[] events = competition.events();
        assertNotNull(events);
        assertEquals(1, events.length);
        Event event = events[0];

        assertEquals(EventType.Individual, event.eventType());
        assertEquals(new Round((byte) 0, RoundType.Final), event.round());
        assertEquals("AK 12", event.agegroup());
        assertEquals(Gender.Female, event.gender());
        assertEquals(InputValueType.Time, event.inputValueType());
    }

    @Test
    void importOneCompetitionWithNoEventAndNoEntry() throws Exception {
        Competition competition = importer.importJson(individualNoEvent);

        Event[] events = competition.events();
        assertNotNull(events);
        assertEquals(0, events.length);
    }

    @Test
    void importCompetitionWithOneEventAndTwoEntries() throws Exception {

        Competition competition = importer.importJson(individualTwoEntries);

        Event[] events = competition.events();
        assertNotNull(events);
        assertEquals(1, events.length);
        Event event = events[0];

        Entry[] entries = event.entries();

        Entry entry1 = entries[0];
        assertEquals("11", entry1.number());
        assertEquals("von Domas, Gerta", entry1.name());
        assertEquals("Neumarkt (ND)", entry1.club());
        assertEquals("", entry1.nationality());
        assertEquals(0, entry1.placeInHeat());
        assertEquals(78530, entry1.timeInMillis());
        assertEquals(new Start("1", (byte) 6), entry1.start());
        assertEquals(1, entry1.penalties().length);
        assertEquals(new Penalty("P321", PenaltyType.Points, (short) 123), entry1.penalties()[0]);

        Entry entry2 = entries[1];
        assertEquals("1", entry2.number());
        assertEquals("Schuster, Corinna", entry2.name());
        assertEquals("Murrhardt (ND)", entry2.club());
        assertEquals("", entry2.nationality());
        assertEquals(0, entry2.placeInHeat());
        assertEquals(54720, entry2.timeInMillis());
        assertEquals(new Start("2", (byte) 1), entry2.start());
        assertEquals(0, entry2.penalties().length);
    }

    @Test
    void importCompetitionWithTwoEventAndTwoEntries() throws Exception {
        Competition competition = importer.importJson(individualTwoEvents);

        Event[] events = competition.events();
        assertNotNull(events);
        assertEquals(2, events.length);
        Event event1 = events[0];
        Event event2 = events[1];

        Entry[] entries1 = event1.entries();
        assertEquals(1, entries1.length);

        Entry entry1 = entries1[0];
        assertEquals("11", entry1.number());
        assertEquals("von Domas, Gerta", entry1.name());
        assertEquals("Neumarkt (ND)", entry1.club());
        assertEquals("", entry1.nationality());
        assertEquals(0, entry1.placeInHeat());
        assertEquals(78530, entry1.timeInMillis());
        assertEquals(new Start("1", (byte) 6), entry1.start());
        assertEquals(0, entry1.penalties().length);

        Entry[] entries2 = event2.entries();
        assertEquals(1, entries2.length);

        Entry entry2 = entries2[0];
        assertEquals("44", entry2.number());
        assertEquals("Schurz, Joachim", entry2.name());
        assertEquals("Altlandsberg (SH)", entry2.club());
        assertEquals("", entry2.nationality());
        assertEquals(0, entry2.placeInHeat());
        assertEquals(63120, entry2.timeInMillis());
        assertEquals(new Start("12", (byte) 5), entry2.start());
        assertEquals(1, entry2.penalties().length);
        assertEquals(new Penalty("W7", PenaltyType.Points, (short) 200), entry2.penalties()[0]);
    }
}
