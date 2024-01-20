package org.lisasp.competition.test.results.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.lisasp.competition.base.api.exception.CompetitionNotFoundException;
import org.lisasp.competition.base.api.exception.InvalidDataException;
import org.lisasp.competition.base.api.exception.NotFoundException;
import org.lisasp.competition.base.api.type.*;
import org.lisasp.competition.results.api.CompetitionDto;
import org.lisasp.competition.results.api.EntryChangeListener;
import org.lisasp.competition.results.api.EntryDto;
import org.lisasp.competition.results.api.EventDto;
import org.lisasp.competition.results.api.imports.Competition;
import org.lisasp.competition.results.api.imports.Entry;
import org.lisasp.competition.results.api.imports.Event;
import org.lisasp.competition.results.api.value.*;
import org.lisasp.competition.results.service.CompetitionChangeAdapter;
import org.lisasp.competition.results.service.ResultService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResultServiceIT {

    private static final LocalDate date1 = LocalDate.of(2023, Month.AUGUST, 3);
    private static final LocalDate date2 = LocalDate.of(2023, Month.AUGUST, 5);

    private EntryChangeListener listener;

    private TestDoubleCompetitionResultRepository competitionRepository;
    private TestDoubleEventResultRepository eventRepository;
    private TestDoubleEntryResultRepository entryRepository;
    private ResultService service;

    @BeforeEach
    void prepare() {
        listener = mock(EntryChangeListener.class);

        competitionRepository = new TestDoubleCompetitionResultRepository();
        eventRepository = new TestDoubleEventResultRepository();
        entryRepository = new TestDoubleEntryResultRepository();

        competitionRepository.setEventRepository(eventRepository);
        eventRepository.setCompetitionRepository(competitionRepository);
        eventRepository.setEntryRepository(entryRepository);
        entryRepository.setEventRepository(eventRepository);

        service = new ResultService(competitionRepository, eventRepository, entryRepository);
        service.register(listener);

        // second register should have no effect
        service.register(listener);
    }

    @Test
    void createAndQueryCompetition() throws Exception {
        final String competitionId = "1";
        service.addOrUpdate(new org.lisasp.competition.api.CompetitionDto(competitionId, 1, "Alphabet", "abc", date1,
                date2));

        CompetitionDto competitionDto = service.findCompetition(competitionId);

        assertNotNull(competitionDto);
        assertEquals("Alphabet", competitionDto.name());
        assertEquals("abc", competitionDto.acronym());
        assertEquals(date1, competitionDto.from());
        assertEquals(date2, competitionDto.till());

        verifyNoMoreInteractions(listener);
    }

    @Test
    void queryCompetitionByUploadId() throws Exception {
        final String competitionId = "1";
        service.addOrUpdate(new org.lisasp.competition.api.CompetitionDto(competitionId, 1, "Alphabet", "abc", date1,
                date2));
        String uploadId = service.getUploadId(competitionId);

        String actualId = service.getCompetitionIdByUploadId(uploadId);

        assertEquals(competitionId, actualId);

        verifyNoMoreInteractions(listener);
    }

    @Test
    void queryCompetitionByWrongUploadId() {
        assertThrows(NotFoundException.class, () -> service.getCompetitionIdByUploadId("1"));
    }

    @Test
    void deleteCompetition() {
        final String competitionId = "1";
        service.addOrUpdate(new org.lisasp.competition.api.CompetitionDto(competitionId, 1, "Alphabet", "abc", date1,
                date2));

        service.delete(competitionId);

        assertEquals(0, competitionRepository.count());
        assertEquals(0, eventRepository.count());
        assertEquals(0, entryRepository.count());

        verifyNoMoreInteractions(listener);
    }

    @Test
    void queryUnknownCompetition() {
        service.addOrUpdate(new org.lisasp.competition.api.CompetitionDto("1", 1, "Alphabet 1", "abc 1", date1, date1));
        service.addOrUpdate(new org.lisasp.competition.api.CompetitionDto("2", 1, "Alphabet 2", "abc 2", date2, date2));

        assertThrows(NotFoundException.class, () -> service.findCompetition("does-not-exist"));
    }

    @Test
    void queryUnknownEvent() {
        assertThrows(NotFoundException.class, () -> service.findEvents("does-not-exist"));
    }

    @Test
    void queryUnknownEntries() {
        assertThrows(NotFoundException.class, () -> service.findEntries("does-not-exist"));
    }

    @Test
    void findCompetitionsEmpty() {
        CompetitionDto[] competitions = service.findCompetitions();

        assertArrayEquals(new CompetitionDto[0], competitions);

        verifyNoMoreInteractions(listener);
    }

    @Test
    void findCompetitionsTwo() {
        assertEquals(0, service.findCompetitions().length);

        service.addOrUpdate(new org.lisasp.competition.api.CompetitionDto("1", 1, "Alphabet 1", "abc 1", date1, date1));
        service.addOrUpdate(new org.lisasp.competition.api.CompetitionDto("2", 1, "Alphabet 2", "abc 2", date2, date2));

        CompetitionDto[] competitions = service.findCompetitions();

        assertEquals(2, competitions.length);
        assertTrue(Arrays.stream(competitions).anyMatch(c -> c.name().equals("Alphabet 1")));
        assertTrue(Arrays.stream(competitions).anyMatch(c -> c.name().equals("Alphabet 2")));

        verifyNoMoreInteractions(listener);
    }

    @Nested
    class UpdateCompetition {

        @Test
        void updateCompetitionWithoutEvents() throws Exception {
            service.addOrUpdate(new org.lisasp.competition.api.CompetitionDto("1", 1, "Alphabet 1", "abc 1", date1,
                    date2));

            Competition competition = new Competition("", "", null, null, new Event[0]);
            service.update("1", competition);

            CompetitionDto competitionDto = service.findCompetition("1");
            assertEquals("Alphabet 1", competitionDto.name());
            assertEquals("abc 1", competitionDto.acronym());
            assertEquals(date1, competitionDto.from());
            assertEquals(date2, competitionDto.till());

            verifyNoMoreInteractions(listener);
        }

        @Test
        void updateCompetitionWithInvalidId() {
            service.addOrUpdate(new org.lisasp.competition.api.CompetitionDto("1", 1, "Alphabet 1", "abc 1", date1,
                    date2));
            Competition competition = new Competition("", "", null, null, new Event[0]);

            assertThrows(InvalidDataException.class, () -> service.update(null, competition));
            assertThrows(InvalidDataException.class, () -> service.update("", competition));
            assertThrows(InvalidDataException.class, () -> service.update("   ", competition));
        }

        @Test
        void updateCompetitionWithWrongId() {
            service.addOrUpdate(new org.lisasp.competition.api.CompetitionDto("1", 1, "Alphabet 1", "abc 1", date1,
                    date2));
            Competition competition = new Competition("", "", null, null, new Event[0]);

            assertThrows(CompetitionNotFoundException.class, () -> service.update("15", competition));
        }

        @Test
        void updateCompetitionWithNull() {
            service.addOrUpdate(new org.lisasp.competition.api.CompetitionDto("1", 1, "Alphabet 1", "abc 1", date1,
                    date2));

            assertThrows(InvalidDataException.class, () -> service.update("1", null));
        }

        @Test
        void updateCompetitionWithNullEvents() throws Exception {
            service.addOrUpdate(new org.lisasp.competition.api.CompetitionDto("1", 1, "Alphabet 1", "abc 1", date1,
                    date2));

            Competition competition = new Competition("", "", null, null, null);
            service.update("1", competition);

            CompetitionDto competitionDto = service.findCompetition("1");
            assertEquals("Alphabet 1", competitionDto.name());
            assertEquals("abc 1", competitionDto.acronym());
            assertEquals(date1, competitionDto.from());
            assertEquals(date2, competitionDto.till());

            verifyNoMoreInteractions(listener);
        }

        @Test
        void updateCompetitionWithNullEntries() throws Exception {
            service.addOrUpdate(new org.lisasp.competition.api.CompetitionDto("1", 1, "Alphabet 1", "abc 1", date1,
                    date2));

            Competition competition = new Competition("", "", null, null, new Event[]{new Event("AG 1",
                    EventType.Individual, Gender.Female, "D1", new Round((byte) 0, RoundType.Final),
                    InputValueType.Time, CourseType.Long, date1, null)});
            service.update("1", competition);

            CompetitionDto competitionDto = service.findCompetition("1");
            assertEquals("Alphabet 1", competitionDto.name());
            assertEquals("abc 1", competitionDto.acronym());
            assertEquals(date1, competitionDto.from());
            assertEquals(date2, competitionDto.till());

            verifyNoMoreInteractions(listener);
        }

        @Test
        void deleteCompetition() throws Exception {
            final String competitionId = "1";
            service.addOrUpdate(new org.lisasp.competition.api.CompetitionDto(competitionId, 1, "Alphabet 1", "abc 1"
                    , date1, date2));

            Competition competition = new Competition("", "", null, null, new Event[]{new Event("AG 1",
                    EventType.Individual, Gender.Female, "D1", new Round((byte) 0, RoundType.Final),
                    InputValueType.Time, CourseType.Long, date1, new Entry[]{new Entry("123", "A", "B", "GER", 123450
                    , (byte) 1, new Penalty[0], new Swimmer[0], new SplitTime[0], new Start("1", (byte) 2))})});
            service.update(competitionId, competition);

            service.delete(competitionId);

            assertEquals(0, competitionRepository.count());
            assertEquals(0, eventRepository.count());
            assertEquals(0, entryRepository.count());

            verify(listener, times(1)).changed(any());
            verifyNoMoreInteractions(listener);
        }

        @Test
        void addEventTest() throws Exception {
            service.addOrUpdate(new org.lisasp.competition.api.CompetitionDto("1", 1, "Alphabet 1", "abc 1", null,
                    null));
            final String competitionId = "1";
            Competition competition = new Competition("", "", null, null, new Event[]{new Event("AG 1",
                    EventType.Individual, Gender.Female, "D1", new Round((byte) 0, RoundType.Final),
                    InputValueType.Time, CourseType.Long, date1, new Entry[0])});

            service.update(competitionId, competition);

            CompetitionDto competitionDto = service.findCompetition(competitionId);
            assertEquals("Alphabet 1", competitionDto.name());

            assertEquals(1, service.findEvents(competitionDto.id()).length);
            EventDto event = service.findEvents(competitionDto.id())[0];
            assertEquals("AG 1", event.agegroup());
            assertEquals(Gender.Female, event.gender());
            assertEquals("D1", event.discipline());
            assertEquals(new Round((byte) 0, RoundType.Final), event.round());
            assertEquals(InputValueType.Time, event.inputValueType());
            assertEquals(0, service.findEntries(event.id()).length);

            verifyNoMoreInteractions(listener);
        }

        @Test
        void findEventTest() throws Exception {
            service.addOrUpdate(new org.lisasp.competition.api.CompetitionDto("1", 1, "Alphabet 1", "abc 1", null,
                    null));
            final String competitionId = "1";
            Competition competition = new Competition("", "", null, null, new Event[]{new Event("AG 1",
                    EventType.Individual, Gender.Female, "D1", new Round((byte) 0, RoundType.Final),
                    InputValueType.Time, CourseType.Long, date1, new Entry[0])});
            service.update(competitionId, competition);

            EventDto[] events = service.findEvents(competitionId);
            assertEquals(1, events.length);

            EventDto event = service.findEvent(events[0].id());

            assertEquals(events[0], event);
        }

        @Test
        void addTwoEventsTest() throws Exception {
            service.addOrUpdate(new org.lisasp.competition.api.CompetitionDto("1", 1, "Alphabet 1", "abc 1", null,
                    null));
            final String competitionId = "1";

            Competition competition = new Competition("", "", null, null, new Event[]{new Event("AG 1",
                    EventType.Individual, Gender.Female, "D1", new Round((byte) 0, RoundType.Final),
                    InputValueType.Time, CourseType.Long, date1, new Entry[0]), new Event("AG 2",
                    EventType.Individual, Gender.Male, "D2", new Round((byte) 1, RoundType.Heat), InputValueType.Rank
                    , CourseType.Long, date1, new Entry[0])});
            service.update(competitionId, competition);

            CompetitionDto competitionDto = service.findCompetition(competitionId);
            assertEquals(competitionId, competitionDto.id());
            assertEquals("Alphabet 1", competitionDto.name());

            EventDto[] events =
                    Arrays.stream(service.findEvents(competitionDto.id())).sorted(Comparator.comparing(EventDto::agegroup)).toArray(EventDto[]::new);
            assertEquals(2, events.length);

            EventDto event1 = events[0];
            assertEquals("AG 1", event1.agegroup());
            assertEquals(Gender.Female, event1.gender());
            assertEquals("D1", event1.discipline());
            assertEquals(new Round((byte) 0, RoundType.Final), event1.round());
            assertEquals(InputValueType.Time, event1.inputValueType());
            assertEquals(0, service.findEntries(event1.id()).length);

            EventDto event2 = events[1];
            assertEquals("AG 2", event2.agegroup());
            assertEquals(Gender.Male, event2.gender());
            assertEquals("D2", event2.discipline());
            assertEquals(new Round((byte) 1, RoundType.Heat), event2.round());
            assertEquals(InputValueType.Rank, event2.inputValueType());
            assertEquals(0, service.findEntries(event2.id()).length);

            verifyNoMoreInteractions(listener);
        }

        @Test
        void removeEventTest() throws Exception {
            service.addOrUpdate(new org.lisasp.competition.api.CompetitionDto("1", 1, "Alphabet 1", "abc 1", null,
                    null));
            final String competitionId = "1";
            Competition competition1 = new Competition("", "", null, null, new Event[]{new Event("AG 1",
                    EventType.Individual, Gender.Female, "D1", new Round((byte) 0, RoundType.Final),
                    InputValueType.Time, CourseType.Long, date1, new Entry[0])});
            Competition competition2 = new Competition("", "", null, null, new Event[0]);

            service.update(competitionId, competition1);
            service.update(competitionId, competition2);

            CompetitionDto competitionDto = service.findCompetition(competitionId);
            assertEquals("Alphabet 1", competitionDto.name());
            assertEquals(0, service.findEvents(competitionDto.id()).length);

            verifyNoMoreInteractions(listener);
        }

        @Test
        @Transactional
        void removeOneOfTwoEventsTest() throws Exception {
            service.addOrUpdate(new org.lisasp.competition.api.CompetitionDto("1", 1, "Alphabet 1", "abc 1", null,
                    null));
            final String competitionId = "1";
            Competition competition1 = new Competition("", "", null, null, new Event[]{new Event("AG 1",
                    EventType.Individual, Gender.Female, "D1", new Round((byte) 0, RoundType.Final),
                    InputValueType.Time, CourseType.Long, date1, new Entry[0]), new Event("AG 2",
                    EventType.Individual, Gender.Male, "D2", new Round((byte) 0, RoundType.Final),
                    InputValueType.Time, CourseType.Long, date1, new Entry[0])});
            Competition competition2 = new Competition("", "", null, null, new Event[]{new Event("AG 1",
                    EventType.Individual, Gender.Female, "D1", new Round((byte) 0, RoundType.Final),
                    InputValueType.Time, CourseType.Long, date1, new Entry[0])});
            service.update(competitionId, competition1);

            service.update(competitionId, competition2);

            CompetitionDto competitionDto = service.findCompetition(competitionId);
            assertEquals("Alphabet 1", competitionDto.name());

            assertEquals(1, service.findEvents(competitionDto.id()).length);
            EventDto event = service.findEvents(competitionDto.id())[0];
            assertEquals("AG 1", event.agegroup());
            assertEquals(Gender.Female, event.gender());
            assertEquals("D1", event.discipline());
            assertEquals(new Round((byte) 0, RoundType.Final), event.round());
            assertEquals(InputValueType.Time, event.inputValueType());
            assertEquals(0, service.findEntries(event.id()).length);

            verifyNoMoreInteractions(listener);
        }

        @Test
        @Transactional
        void addEntryTest() throws Exception {
            service.addOrUpdate(new org.lisasp.competition.api.CompetitionDto("1", 1, "Alphabet 1", "abc 1", null,
                    null));
            final String competitionId = "1";

            Competition competition = new Competition("", "", null, null, new Event[]{new Event("AG 1",
                    EventType.Individual, Gender.Female, "D1", new Round((byte) 0, RoundType.Final),
                    InputValueType.Time, CourseType.Long, date1, new Entry[]{new Entry("123", "A", "B", "GER", 123450
                    , (byte) 1, new Penalty[0], new Swimmer[0], new SplitTime[0], new Start("1", (byte) 2))})});
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

        @Test
        @Transactional
        void removeEntryTest() throws Exception {
            service.addOrUpdate(new org.lisasp.competition.api.CompetitionDto("1", 1, "Alphabet 1", "abc 1", null,
                    null));
            final String competitionId = "1";
            Competition competition1 = new Competition("", "", null, null, new Event[]{new Event("AG 1",
                    EventType.Individual, Gender.Female, "D1", new Round((byte) 0, RoundType.Final),
                    InputValueType.Time, CourseType.Long, date1, new Entry[]{new Entry("123", "A", "B", "GER", 123450, (byte) 1,
                    new Penalty[0], new Swimmer[0], new SplitTime[0], new Start("1", (byte) 2))})});
            Competition competition2 = new Competition("", "", null, null, new Event[]{new Event("AG 1",
                    EventType.Individual, Gender.Female, "D1", new Round((byte) 0, RoundType.Final),
                    InputValueType.Time, CourseType.Long, date1, new Entry[0])});
            service.update(competitionId, competition1);

            service.update(competitionId, competition2);

            CompetitionDto competitionDto = service.findCompetition(competitionId);
            assertEquals("Alphabet 1", competitionDto.name());

            assertEquals(1, service.findEvents(competitionDto.id()).length);
            EventDto event = service.findEvents(competitionDto.id())[0];

            assertEquals(0, service.findEntries(event.id()).length);

            verify(listener, times(2)).changed(any());
            verifyNoMoreInteractions(listener);
        }

        @Test
        @Transactional
        void addTwoEntriesTest() throws Exception {
            service.addOrUpdate(new org.lisasp.competition.api.CompetitionDto("1", 1, "Alphabet 1", "abc 1", null,
                    null));
            final String competitionId = "1";

            Competition competition = new Competition("", "", null, null, new Event[]{new Event("AG 1",
                    EventType.Individual, Gender.Female, "D1", new Round((byte) 0, RoundType.Final),
                    InputValueType.Time, CourseType.Long, date1, new Entry[]{new Entry("123", "A", "B", "GER", 123450, (byte) 1,
                    new Penalty[0], new Swimmer[0], new SplitTime[0], new Start("1", (byte) 2)), new Entry("456", "C"
                    , "D", "GER", 543210, (byte) 1, new Penalty[0], new Swimmer[0], new SplitTime[0], new Start("1",
                    (byte) 2))})});

            service.update(competitionId, competition);

            CompetitionDto competitionDto = service.findCompetition(competitionId);
            assertEquals("Alphabet 1", competitionDto.name());

            assertEquals(1, service.findEvents(competitionDto.id()).length);
            EventDto event = service.findEvents(competitionDto.id())[0];

            EntryDto[] entries =
                    Arrays.stream(service.findEntries(event.id())).sorted(Comparator.comparing(EntryDto::number)).toArray(EntryDto[]::new);
            assertEquals(2, entries.length);

            EntryDto entry1 = entries[0];
            assertEquals("123", entry1.number());
            assertEquals(123450, entry1.timeInMillis());
            assertEquals("A", entry1.name());
            assertEquals("B", entry1.club());
            assertEquals("GER", entry1.nationality());

            EntryDto entry2 = entries[1];
            assertEquals("456", entry2.number());
            assertEquals(543210, entry2.timeInMillis());
            assertEquals("C", entry2.name());
            assertEquals("D", entry2.club());
            assertEquals("GER", entry2.nationality());

            verify(listener, times(2)).changed(any());
            verifyNoMoreInteractions(listener);
        }

        @Test
        @Transactional
        void updateWithSameDataTwice() throws Exception {
            service.addOrUpdate(new org.lisasp.competition.api.CompetitionDto("1", 1, "Alphabet 1", "abc 1", null,
                    null));
            final String competitionId = "1";

            Competition competition = new Competition("", "", null, null, new Event[]{new Event("AG 1",
                    EventType.Individual, Gender.Female, "D1", new Round((byte) 0, RoundType.Final),
                    InputValueType.Time, CourseType.Long, date1, new Entry[]{new Entry("123", "A", "B", "GER", 123450, (byte) 1,
                    new Penalty[0], new Swimmer[0], new SplitTime[0], new Start("1", (byte) 2)), new Entry("456", "C"
                    , "D", "GER", 543210, (byte) 1, new Penalty[0], new Swimmer[0], new SplitTime[0], new Start("1",
                    (byte) 2))})});

            service.update(competitionId, competition);
            service.update(competitionId, competition);

            CompetitionDto competitionDto = service.findCompetition(competitionId);
            assertEquals("Alphabet 1", competitionDto.name());

            assertEquals(1, service.findEvents(competitionDto.id()).length);
            EventDto event = service.findEvents(competitionDto.id())[0];

            EntryDto[] entries =
                    Arrays.stream(service.findEntries(event.id())).sorted(Comparator.comparing(EntryDto::number)).toArray(EntryDto[]::new);
            assertEquals(2, entries.length);

            EntryDto entry1 = entries[0];
            assertEquals("123", entry1.number());
            assertEquals(123450, entry1.timeInMillis());
            assertEquals("A", entry1.name());
            assertEquals("B", entry1.club());
            assertEquals("GER", entry1.nationality());

            EntryDto entry2 = entries[1];
            assertEquals("456", entry2.number());
            assertEquals(543210, entry2.timeInMillis());
            assertEquals("C", entry2.name());
            assertEquals("D", entry2.club());
            assertEquals("GER", entry2.nationality());

            verify(listener, times(2)).changed(any());
            verifyNoMoreInteractions(listener);
        }

        @Test
        @Transactional
        void removeOneOfTwoEntriesTest() throws Exception {
            service.addOrUpdate(new org.lisasp.competition.api.CompetitionDto("1", 1, "Alphabet 1", "abc 1", null,
                    null));
            final String competitionId = "1";
            Competition competition1 = new Competition("", "", null, null, new Event[]{new Event("AG 1",
                    EventType.Individual, Gender.Female, "D1", new Round((byte) 0, RoundType.Final),
                    InputValueType.Time, CourseType.Long, date1, new Entry[]{new Entry("123", "A", "B", "GER", 123450, (byte) 1,
                    new Penalty[0], new Swimmer[0], new SplitTime[0], new Start("1", (byte) 2)), new Entry("456", "C"
                    , "D", "GER", 543210, (byte) 1, new Penalty[0], new Swimmer[0], new SplitTime[0], new Start("1",
                    (byte) 2))})});
            Competition competition2 = new Competition("", "", null, null, new Event[]{new Event("AG 1",
                    EventType.Individual, Gender.Female, "D1", new Round((byte) 0, RoundType.Final),
                    InputValueType.Time, CourseType.Long, date1, new Entry[]{new Entry("123", "A", "B", "GER", 123450, (byte) 1,
                    new Penalty[0], new Swimmer[0], new SplitTime[0], new Start("1", (byte) 2))})});

            service.update(competitionId, competition1);

            service.update(competitionId, competition2);

            CompetitionDto competitionDto = service.findCompetition(competitionId);
            assertEquals("Alphabet 1", competitionDto.name());

            assertEquals(1, service.findEvents(competitionDto.id()).length);
            EventDto event = service.findEvents(competitionDto.id())[0];

            EntryDto[] entries =
                    Arrays.stream(service.findEntries(event.id())).sorted(Comparator.comparing(EntryDto::number)).toArray(EntryDto[]::new);
            assertEquals(1, entries.length);

            EntryDto entry1 = entries[0];
            assertEquals("123", entry1.number());
            assertEquals(123450, entry1.timeInMillis());
            assertEquals("A", entry1.name());
            assertEquals("B", entry1.club());
            assertEquals("GER", entry1.nationality());

            verify(listener, times(3)).changed(any());
            verifyNoMoreInteractions(listener);
        }
    }

    @Nested
    class ChangeListenerTests {

        @Test
        void createdEventTest() throws NotFoundException {
            CompetitionChangeAdapter changeListener = new CompetitionChangeAdapter(service);
            changeListener.added(new org.lisasp.competition.api.CompetitionDto("1", 1, "Created", "CRT", date1, date1));

            CompetitionDto dto = service.findCompetition("1");
            assertNotNull(dto);
            assertEquals("1", dto.id());
            assertEquals(1, dto.version());
            assertEquals("Created", dto.name());
            assertEquals("CRT", dto.acronym());
            assertEquals(date1, dto.from());
            assertEquals(date1, dto.till());
        }

        @Test
        void updatedEventTest() throws NotFoundException {
            CompetitionChangeAdapter changeListener = new CompetitionChangeAdapter(service);
            changeListener.added(new org.lisasp.competition.api.CompetitionDto("1", 1, "Created", "CRT", date1, date1));
            changeListener.updated(new org.lisasp.competition.api.CompetitionDto("1", 2, "Updated", "UPD", date2,
                    date2));

            CompetitionDto dto = service.findCompetition("1");
            assertNotNull(dto);
            assertEquals("1", dto.id());
            assertEquals(2, dto.version());
            assertEquals("Updated", dto.name());
            assertEquals("UPD", dto.acronym());
            assertEquals(date2, dto.from());
            assertEquals(date2, dto.till());
        }

        @Test
        void deletedEventTest() {
            CompetitionChangeAdapter changeListener = new CompetitionChangeAdapter(service);
            changeListener.added(new org.lisasp.competition.api.CompetitionDto("1", 1, "Created", "CRT", date1, date1));
            changeListener.deleted(new org.lisasp.competition.api.CompetitionDto("1", 1, "Created", "CRT", date1,
                    date1));

            assertArrayEquals(new CompetitionDto[0], service.findCompetitions());
        }
    }
}
