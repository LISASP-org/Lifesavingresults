package org.lisasp.competition;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.lisasp.competition.base.api.type.EventType;
import org.lisasp.competition.base.api.type.Gender;
import org.lisasp.competition.base.api.type.InputValueType;
import org.lisasp.competition.base.api.type.RoundType;
import org.lisasp.competition.results.api.CompetitionDto;
import org.lisasp.competition.results.api.EntryDto;
import org.lisasp.competition.results.api.EventDto;
import org.lisasp.competition.results.api.EntryChangeListener;
import org.lisasp.competition.results.api.imports.Competition;
import org.lisasp.competition.results.api.imports.Entry;
import org.lisasp.competition.results.api.imports.Event;
import org.lisasp.competition.results.api.value.*;
import org.lisasp.competition.results.service.CompetitionResultRepository;
import org.lisasp.competition.results.service.EntryResultRepository;
import org.lisasp.competition.results.service.EventResultRepository;
import org.lisasp.competition.results.service.ResultService;
import org.lisasp.competition.service.CompetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("IntegrationTest")
@SpringBootTest
class CompetitionServiceIntegrationTests {

    private static final LocalDate date1 = LocalDate.of(2023, Month.JUNE, 17);
    private static final LocalDate date2 = LocalDate.of(2023, Month.JUNE, 19);

    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private CompetitionResultRepository competitionResultRepository;
    @Autowired
    private EventResultRepository eventResultRepository;
    @Autowired
    private EntryResultRepository entryResultRepository;

    @Autowired
    private ResultService service;

    @Autowired
    private DatabaseCleaner cleaner;

    private EntryChangeListener listener;

    @BeforeEach
    void prepare() {
        listener = mock(EntryChangeListener.class);
        service.register(listener);

        cleaner = new DatabaseCleaner(competitionRepository, competitionResultRepository, eventResultRepository, entryResultRepository);
    }

    @AfterEach
    void cleanup() {
        cleaner.clean();
    }

    @Test
    @Transactional
    void createAndQueryCompetition() throws Exception {
        service.addOrUpdate(new org.lisasp.competition.api.CompetitionDto("12345", 1, "Alphabet", "abc", date1, date2));

        CompetitionDto competitionDto = service.findCompetition("12345");

        assertNotNull(competitionDto);
        assertEquals("Alphabet", competitionDto.name());
        assertEquals("abc", competitionDto.acronym());

        verifyNoMoreInteractions(listener);
    }

    @Test
    @Transactional
    void findCompetitionByUploadIdTest() throws Exception {
        service.addOrUpdate(new org.lisasp.competition.api.CompetitionDto("1", 1, "Alphabet 1", "abc 1", date1, date2));

        String actual = service.getUploadId("1");

        assertNotNull(actual);
        assertTrue(30 < actual.length());

        verifyNoMoreInteractions(listener);
    }

    @Test
    @Transactional
    void updateCompetitionTest() throws Exception {
        service.addOrUpdate(new org.lisasp.competition.api.CompetitionDto("1", 1, "Alphabet 1", "abc 1", date1, date2));
        final String competitionId = "1";

        Competition competition = new Competition("",
                                                  "",
                                                  null,
                                                  null,
                                                  new Event[]{new Event("AG 1",
                                                                        EventType.Individual,
                                                                        Gender.Female,
                                                                        "D1",
                                                                        new Round((byte) 0, RoundType.Final),
                                                                        InputValueType.Time,
                                                                        new Entry[]{new Entry("123",
                                                                                              "A",
                                                                                              "B",
                                                                                              "GER",
                                                                                              123450,
                                                                                              (byte) 1,
                                                                                              new Penalty[0],
                                                                                              new Swimmer[0],
                                                                                              new SplitTime[0],
                                                                                              new Start("1", (byte) 2))})});
        service.update(competitionId, competition);

        CompetitionDto competitionDto = service.findCompetition(competitionId);
        assertEquals("Alphabet 1", competitionDto.name());

        assertEquals(1, service.findEvents(competitionDto.id()).length);
        EventDto event = service.findEvents(competitionDto.id())[0];

        assertEquals(1, service.findEntries(event.id()).length);
        EntryDto entry = service.findEntries(event.id())[0];
        assertEquals("123", entry.number());
        assertEquals(123450, entry.timeInMillis());
        assertEquals("A", entry.name());
        assertEquals("B", entry.club());
        assertEquals("GER", entry.nationality());

        verify(listener, times(1)).changed(any());
        verifyNoMoreInteractions(listener);
    }
}
