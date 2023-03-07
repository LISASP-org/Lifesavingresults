package org.lisasp.results.test.imports.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lisasp.results.api.*;
import org.lisasp.results.api.type.EventType;
import org.lisasp.results.api.type.Gender;
import org.lisasp.results.api.type.InputValueType;
import org.lisasp.results.api.value.Round;
import org.lisasp.results.api.value.Start;
import org.lisasp.results.service.imports.ImportConfiguration;
import org.lisasp.results.service.imports.ImportService;
import org.lisasp.results.service.imports.ImportStorage;
import org.lisasp.results.service.CompetitionService;
import org.lisasp.results.service.EntryService;
import org.lisasp.results.service.EventService;
import org.lisasp.results.test.model.TestDoubleCompetitionRepository;
import org.lisasp.results.test.model.TestDoubleEntryRepository;
import org.lisasp.results.test.model.TestDoubleEventRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ImportServiceTest {
    @Autowired
    private ImportService service;

    private CompetitionService competitionService;
    private EventService eventService;
    private EntryService entryService;

    private String storagePath;

    @BeforeEach
    void prepare() throws IOException {
        Path path = Files.createTempDirectory("import-service");
        storagePath = path.toString();

        ImportConfiguration config = () -> storagePath;

        TestDoubleEntryRepository entryRepository = new TestDoubleEntryRepository();
        TestDoubleEventRepository eventRepository = new TestDoubleEventRepository();
        TestDoubleCompetitionRepository competitionRepository = new TestDoubleCompetitionRepository();

        competitionRepository.setEventRepository(eventRepository);
        eventRepository.setCompetitionRepository(competitionRepository);
        eventRepository.setEntryRepository(entryRepository);
        entryRepository.setEventRepository(eventRepository);

        competitionService = new CompetitionService(competitionRepository, eventRepository, entryRepository);
        eventService = new EventService(competitionRepository, eventRepository);
        entryService = new EntryService(eventRepository, entryRepository);

        service = new ImportService(competitionService, new ImportStorage(config));
    }

    @Test
    void importSimpleCompetition() throws Exception {
        CompetitionCreated created = competitionService.execute(new CreateCompetition("Competition to import", "CTI", null, null));
        String uploadId = competitionService.findCompetition(created.id()).getUploadId();
        String content = readFile("individual-no-event");

        service.importFromJAuswertung(uploadId, content);

        CompetitionDto competition = competitionService.findCompetition(created.id());
        assertNotNull(competition);
        assertEquals(created.id(), competition.getId());

        assertEquals(0, eventService.findEvents(created.id()).length);
    }

    @Test
    void importCompleteTeamCompetition() throws Exception {
        CompetitionCreated created = competitionService.execute(new CreateCompetition("Competition to import", "CTI", null, null));
        String competitionId = created.id();
        String uploadId = competitionService.findCompetition(competitionId).getUploadId();
        String content = readFile("team");

        service.importFromJAuswertung(uploadId, content);

        CompetitionDto competition = competitionService.findCompetition(competitionId);
        assertNotNull(competition);
        assertEquals(competitionId, competition.getId());

        EventDto[] events = Arrays.stream(eventService.findEvents(competitionId)).sorted(Comparator.comparing(EventDto::getAgegroup)).toArray(EventDto[]::new);
        assertEquals(2, events.length);

        EventDto event1 = events[0];
        assertEquals("AK 12", event1.getAgegroup());
        assertEquals("4*50m Hindernisstaffel", event1.getDiscipline());
        assertEquals(EventType.Team, event1.getEventType());
        assertEquals(Gender.Female, event1.getGender());
        assertEquals(InputValueType.Time, event1.getInputValueType());
        assertEquals(new Round((byte)0, true), event1.getRound());
        EntryDto[] entries1 = Arrays.stream(entryService.findEntries(competitionId, event1.getId())).sorted(Comparator.comparing(EntryDto::getNumber)).toArray(EntryDto[]::new);
        assertEquals(2, entries1.length);
        EntryDto entry1a = entries1[0];
        assertEquals("Geilenkirchen 1", entry1a.getName());
        assertEquals("Geilenkirchen (SA)", entry1a.getClub());
        assertEquals("", entry1a.getNationality());
        assertEquals("1", entry1a.getNumber());
        assertEquals(0, entry1a.getPlaceInHeat());
        assertEquals(new Start("1",(byte)1), entry1a.getStart());
        assertEquals(4, entry1a.getSwimmer().length);
        assertEquals(182990, entry1a.getTimeInMillis());
        EntryDto entry1b = entries1[1];
        assertEquals("Miesbach 11", entry1b.getName());
        assertEquals("Miesbach (WE)", entry1b.getClub());
        assertEquals("", entry1b.getNationality());
        assertEquals("11", entry1b.getNumber());
        assertEquals(0, entry1b.getPlaceInHeat());
        assertEquals(new Start("1",(byte)4), entry1b.getStart());
        assertEquals(4, entry1b.getSwimmer().length);
        assertEquals(181830, entry1b.getTimeInMillis());


        EventDto event2 = events[1];
        assertEquals("AK 15/16", event2.getAgegroup());
        assertEquals("4*50m Hindernisstaffel", event2.getDiscipline());
        assertEquals(EventType.Team, event2.getEventType());
        assertEquals(Gender.Female, event2.getGender());
        assertEquals(InputValueType.Time, event2.getInputValueType());
        assertEquals(new Round((byte)0, true), event2.getRound());
        EntryDto[] entries2 = Arrays.stream(entryService.findEntries(competitionId, event2.getId())).sorted(Comparator.comparing(EntryDto::getNumber)).toArray(EntryDto[]::new);
        assertEquals(2, entries1.length);
        EntryDto entry2a = entries2[0];
        assertEquals("Münster 7", entry2a.getName());
        assertEquals("Münster (ND)", entry2a.getClub());
        assertEquals("", entry2a.getNationality());
        assertEquals("55", entry2a.getNumber());
        assertEquals(0, entry2a.getPlaceInHeat());
        assertEquals(new Start("9",(byte)6), entry2a.getStart());
        assertEquals(4, entry2a.getSwimmer().length);
        assertEquals(177310, entry2a.getTimeInMillis());
        EntryDto entry2b = entries2[1];
        assertEquals("Vöhringen 8", entry2b.getName());
        assertEquals("Vöhringen (HH)", entry2b.getClub());
        assertEquals("", entry2b.getNationality());
        assertEquals("56", entry2b.getNumber());
        assertEquals(0, entry2b.getPlaceInHeat());
        assertEquals(new Start("10",(byte)5), entry2b.getStart());
        assertEquals(4, entry2b.getSwimmer().length);
        assertEquals(166370, entry2b.getTimeInMillis());
    }

    private String readFile(String name) throws IOException {
        return Files.readString(Path.of("./src/test/resources/", String.format("%s.json", name)));
    }
}
