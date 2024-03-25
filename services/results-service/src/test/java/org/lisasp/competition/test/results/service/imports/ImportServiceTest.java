package org.lisasp.competition.test.results.service.imports;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.lisasp.competition.base.api.exception.InvalidDataException;
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

import static org.junit.jupiter.api.Assertions.*;

public class ImportServiceTest {

    private ImportService service;

    private ResultService resultService;

    @TempDir
    private Path storagePath;

    @BeforeEach
    void prepare() {
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

    @ParameterizedTest
    @ValueSource(strings = {"", "-", "{ abc", "abc }"})
    void importInvalidJson(String content) throws Exception {
        final String competitionId = "1";
        resultService.addOrUpdate(new org.lisasp.competition.api.CompetitionDto(competitionId, 1, "Competition to import", "CTI", null, null));
        String uploadId = resultService.getUploadId(competitionId);

        assertThrows(InvalidDataException.class, () -> service.importCompetition(uploadId, 0, content));
    }

    @Test
    void importSimpleCompetition() throws Exception {
        final String competitionId = "1";
        resultService.addOrUpdate(new org.lisasp.competition.api.CompetitionDto(competitionId, 1, "Competition to import", "CTI", null, null));
        String uploadId = resultService.getUploadId(competitionId);
        String content = readFile("individual-no-event");

        service.importCompetition(uploadId, 0, content);

        CompetitionDto competition = resultService.findCompetition(competitionId);
        assertNotNull(competition);
        assertEquals(competitionId, competition.id());

        assertEquals(0, resultService.findEvents(competitionId).length);
    }

    @Test
    void importSimpleCompetitionTwice() throws Exception {
        final String competitionId = "1";
        resultService.addOrUpdate(new org.lisasp.competition.api.CompetitionDto(competitionId, 1, "Competition to import", "CTI", null, null));
        String uploadId = resultService.getUploadId(competitionId);
        String content = readFile("individual-no-event");

        service.importCompetition(uploadId, 0, content);
        service.importCompetition(uploadId, 0, content);

        CompetitionDto competition = resultService.findCompetition(competitionId);
        assertNotNull(competition);
        assertEquals(competitionId, competition.id());

        assertEquals(0, resultService.findEvents(competitionId).length);
    }

    @Test
    void importSimpleCompetitionWithTwoIndices() throws Exception {
        final String competitionId = "1";
        resultService.addOrUpdate(new org.lisasp.competition.api.CompetitionDto(competitionId, 1, "Competition to import", "CTI", null, null));
        String uploadId = resultService.getUploadId(competitionId);
        String content = readFile("individual-no-event");

        service.importCompetition(uploadId, 0, content);
        service.importCompetition(uploadId, 1, content);

        CompetitionDto competition = resultService.findCompetition(competitionId);
        assertNotNull(competition);
        assertEquals(competitionId, competition.id());

        assertEquals(0, resultService.findEvents(competitionId).length);
    }

    @Test
    void importCompleteTeamCompetition() throws Exception {
        final String competitionId = "1";
        resultService.addOrUpdate(new org.lisasp.competition.api.CompetitionDto(competitionId, 1, "Competition to import", "CTI", null, null));
        String uploadId = resultService.getUploadId(competitionId);
        String content = readFile("team");

        service.importCompetition(uploadId, 0, content);

        CompetitionDto competition = resultService.findCompetition(competitionId);
        assertNotNull(competition);
        assertEquals(competitionId, competition.id());
        assertEquals("Competition to import", competition.name());
        assertEquals("CTI", competition.acronym());

        EventDto[] events = Arrays.stream(resultService.findEvents(competitionId)).sorted(Comparator.comparing(EventDto::gender)).toArray(EventDto[]::new);
        assertEquals(2, events.length);

        EventDto event1 = events[0];
        assertEquals("Open", event1.agegroup());
        assertEquals("4x50 Obstacle Relay", event1.discipline());
        assertEquals(EventType.Team, event1.eventType());
        assertEquals(Gender.Female, event1.gender());
        assertEquals(InputValueType.Time, event1.inputValueType());
        assertEquals(new Round((byte) 1, RoundType.Heat), event1.round());
        EntryDto[] entries1 = Arrays.stream(resultService.findEntries(event1.id())).sorted(Comparator.comparing(EntryDto::number)).toArray(EntryDto[]::new);
        assertEquals(1, entries1.length);
        EntryDto entry1 = entries1[0];
        assertEquals("New Zealand", entry1.name());
        assertEquals("New Zealand", entry1.club());
        assertEquals("NZL", entry1.nationality());
        assertEquals("5303", entry1.number());
        assertEquals(1, entry1.placeInHeat());
        assertEquals(new Start("2", (byte) 5), entry1.start());
        assertEquals(0, entry1.swimmer().length);
        assertEquals(113510, entry1.timeInMillis());

        EventDto event2 = events[1];
        assertEquals("Open", event2.agegroup());
        assertEquals("4x50 Obstacle Relay", event2.discipline());
        assertEquals(EventType.Team, event2.eventType());
        assertEquals(Gender.Male, event2.gender());
        assertEquals(InputValueType.Time, event2.inputValueType());
        assertEquals(new Round((byte) 1, RoundType.Heat), event2.round());
        EntryDto[] entries2 = Arrays.stream(resultService.findEntries(event2.id())).sorted(Comparator.comparing(EntryDto::number)).toArray(EntryDto[]::new);
        assertEquals(1, entries2.length);
        EntryDto entry2 = entries2[0];
        assertEquals("Australia", entry2.name());
        assertEquals("Australia", entry2.club());
        assertEquals("AUS", entry2.nationality());
        assertEquals("5296", entry2.number());
        assertEquals(2, entry2.placeInHeat());
        assertEquals(new Start("1", (byte) 4), entry2.start());
        assertEquals(0, entry2.swimmer().length);
        assertEquals(113540, entry2.timeInMillis());
    }

    private String readFile(String name) throws IOException {
        return Files.readString(Path.of("./src/test/resources/", String.format("%s.json", name)));
    }
}
