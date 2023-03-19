package org.lisasp.competition.test.results.service.imports.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lisasp.competition.base.api.type.EventType;
import org.lisasp.competition.base.api.type.Gender;
import org.lisasp.competition.base.api.type.InputValueType;
import org.lisasp.competition.base.api.type.RoundType;
import org.lisasp.competition.results.api.CompetitionDto;
import org.lisasp.competition.results.api.EntryDto;
import org.lisasp.competition.results.api.EventDto;
import org.lisasp.competition.results.api.value.Round;
import org.lisasp.competition.results.api.value.Start;
import org.lisasp.competition.results.service.ResultService;
import org.lisasp.competition.results.service.imports.ImportConfiguration;
import org.lisasp.competition.results.service.imports.ImportService;
import org.lisasp.competition.results.service.imports.ImportStorage;
import org.lisasp.competition.test.results.service.TestDoubleCompetitionResultRepository;
import org.lisasp.competition.test.results.service.TestDoubleEntryResultRepository;
import org.lisasp.competition.test.results.service.TestDoubleEventResultRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ImportServiceTest {

    private ImportService service;

    private ResultService resultService;

    private String storagePath;

    @BeforeEach
    void prepare() throws IOException {
        Path path = Files.createTempDirectory("import-service");
        storagePath = path.toString();

        ImportConfiguration config = () -> storagePath;

        TestDoubleEntryResultRepository entryRepository = new TestDoubleEntryResultRepository();
        TestDoubleEventResultRepository eventRepository = new TestDoubleEventResultRepository();
        TestDoubleCompetitionResultRepository competitionRepository = new TestDoubleCompetitionResultRepository();

        competitionRepository.setEventRepository(eventRepository);
        eventRepository.setCompetitionRepository(competitionRepository);
        eventRepository.setEntryRepository(entryRepository);
        entryRepository.setEventRepository(eventRepository);

        resultService = new ResultService(competitionRepository, eventRepository, entryRepository);

        service = new ImportService(resultService, new ImportStorage(config));
    }

    @Test
    void importSimpleCompetition() throws Exception {
        final String competitionId = "1";
        resultService.addOrUpdate(new org.lisasp.competition.api.CompetitionDto(competitionId, 1, "Competition to import", "CTI", null, null));
        String uploadId = resultService.findCompetition(competitionId).uploadId();
        String content = readFile("individual-no-event");

        service.importFromJAuswertung(uploadId, content);

        CompetitionDto competition = resultService.findCompetition(competitionId);
        assertNotNull(competition);
        assertEquals(competitionId, competition.id());

        assertEquals(0, resultService.findEvents(competitionId).length);
    }

    @Test
    void importCompleteTeamCompetition() throws Exception {
        final String competitionId = "1";
        resultService.addOrUpdate(new org.lisasp.competition.api.CompetitionDto(competitionId, 1, "Competition to import", "CTI", null, null));
        String uploadId = resultService.findCompetition(competitionId).uploadId();
        String content = readFile("team");

        service.importFromJAuswertung(uploadId, content);

        CompetitionDto competition = resultService.findCompetition(competitionId);
        assertNotNull(competition);
        assertEquals(competitionId, competition.id());
        assertEquals("Competition to import", competition.name());
        assertEquals("CTI", competition.acronym());

        EventDto[] events = Arrays.stream(resultService.findEvents(competitionId)).sorted(Comparator.comparing(EventDto::agegroup)).toArray(EventDto[]::new);
        assertEquals(2, events.length);

        EventDto event1 = events[0];
        assertEquals("AK 12", event1.agegroup());
        assertEquals("4*50m Hindernisstaffel", event1.discipline());
        assertEquals(EventType.Team, event1.eventType());
        assertEquals(Gender.Female, event1.gender());
        assertEquals(InputValueType.Time, event1.inputValueType());
        assertEquals(new Round((byte) 0, RoundType.Final), event1.round());
        EntryDto[] entries1 = Arrays.stream(resultService.findEntries(competitionId, event1.id()))
                                    .sorted(Comparator.comparing(EntryDto::number))
                                    .toArray(EntryDto[]::new);
        assertEquals(2, entries1.length);
        EntryDto entry1a = entries1[0];
        assertEquals("Geilenkirchen 1", entry1a.name());
        assertEquals("Geilenkirchen (SA)", entry1a.club());
        assertEquals("", entry1a.nationality());
        assertEquals("1", entry1a.number());
        assertEquals(0, entry1a.placeInHeat());
        assertEquals(new Start("1", (byte) 1), entry1a.start());
        assertEquals(4, entry1a.swimmer().length);
        assertEquals(182990, entry1a.timeInMillis());
        EntryDto entry1b = entries1[1];
        assertEquals("Miesbach 11", entry1b.name());
        assertEquals("Miesbach (WE)", entry1b.club());
        assertEquals("", entry1b.nationality());
        assertEquals("11", entry1b.number());
        assertEquals(0, entry1b.placeInHeat());
        assertEquals(new Start("1", (byte) 4), entry1b.start());
        assertEquals(4, entry1b.swimmer().length);
        assertEquals(181830, entry1b.timeInMillis());


        EventDto event2 = events[1];
        assertEquals("AK 15/16", event2.agegroup());
        assertEquals("4*50m Hindernisstaffel", event2.discipline());
        assertEquals(EventType.Team, event2.eventType());
        assertEquals(Gender.Female, event2.gender());
        assertEquals(InputValueType.Time, event2.inputValueType());
        assertEquals(new Round((byte) 0, RoundType.Final), event2.round());
        EntryDto[] entries2 = Arrays.stream(resultService.findEntries(competitionId, event2.id()))
                                    .sorted(Comparator.comparing(EntryDto::number))
                                    .toArray(EntryDto[]::new);
        assertEquals(2, entries1.length);
        EntryDto entry2a = entries2[0];
        assertEquals("Münster 7", entry2a.name());
        assertEquals("Münster (ND)", entry2a.club());
        assertEquals("", entry2a.nationality());
        assertEquals("55", entry2a.number());
        assertEquals(0, entry2a.placeInHeat());
        assertEquals(new Start("9", (byte) 6), entry2a.start());
        assertEquals(4, entry2a.swimmer().length);
        assertEquals(177310, entry2a.timeInMillis());
        EntryDto entry2b = entries2[1];
        assertEquals("Vöhringen 8", entry2b.name());
        assertEquals("Vöhringen (HH)", entry2b.club());
        assertEquals("", entry2b.nationality());
        assertEquals("56", entry2b.number());
        assertEquals(0, entry2b.placeInHeat());
        assertEquals(new Start("10", (byte) 5), entry2b.start());
        assertEquals(4, entry2b.swimmer().length);
        assertEquals(166370, entry2b.timeInMillis());
    }

    private String readFile(String name) throws IOException {
        return Files.readString(Path.of("./src/test/resources/", String.format("%s.json", name)));
    }
}