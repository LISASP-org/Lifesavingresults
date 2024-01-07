package org.lisasp.competition.test.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lisasp.competition.api.CompetitionChangeListener;
import org.lisasp.competition.api.CompetitionDto;
import org.lisasp.competition.api.CreateCompetition;
import org.lisasp.competition.base.api.exception.NotFoundException;
import org.lisasp.competition.service.CompetitionService;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompetitionServiceTests {

    private static final LocalDate date1 = LocalDate.of(2023, Month.MARCH, 18);
    private static final LocalDate date2 = LocalDate.of(2023, Month.MARCH, 20);

    private CompetitionService service;
    @Mock
    private CompetitionChangeListener listener;

    @BeforeEach
    void prepare() {
        TestDoubleCompetitionRepository competitionRepository = new TestDoubleCompetitionRepository();

        service = new CompetitionService(competitionRepository);
        service.register(listener);
    }

    @Test
    void createAndQueryCompetition() throws Exception {
        CompetitionDto created = service.create(new CreateCompetition("Alphabet", "abc", date1, date2));
        assertNotNull(created);
        assertNotNull(created.id());

        CompetitionDto competitionDto = service.findCompetition(created.id());

        assertNotNull(competitionDto);
        assertEquals("Alphabet", competitionDto.name());
        assertEquals("abc", competitionDto.acronym());
        assertEquals(date1, competitionDto.from());
        assertEquals(date2, competitionDto.till());

        verify(listener, times(1)).added(competitionDto);
        verifyNoMoreInteractions(listener);
    }

    @Test
    void updateCompetition() throws Exception {
        CompetitionDto created = service.create(new CreateCompetition("Alphabet", "abc", date1, date1));

        CompetitionDto competitionDto = service.findCompetition(created.id());
        CompetitionDto update = new CompetitionDto(competitionDto.id(), competitionDto.version(), "Updated", "upd", date2, date2);
        CompetitionDto updated = service.update(update);

        assertNotNull(updated);
        assertEquals("Updated", updated.name());
        assertEquals("upd", updated.acronym());
        assertEquals(date2, updated.from());
        assertEquals(date2, updated.till());

        CompetitionDto updatedAndFound = service.findCompetition(created.id());
        assertNotNull(updatedAndFound);
        assertEquals("Updated", updatedAndFound.name());
        assertEquals("upd", updatedAndFound.acronym());

        verify(listener, times(1)).added(created);
        verify(listener, times(1)).updated(updated);
        verifyNoMoreInteractions(listener);
    }

    @Test
    void updateCompetitionWithWrongId() {
        CompetitionDto update = new CompetitionDto("1", 1, "Updated", "upd", date2, date2);
        assertThrows(NotFoundException.class, () -> service.update(update));
    }

    @Test
    void deleteCompetition() throws Exception {
        CompetitionDto created = service.create(new CreateCompetition("Alphabet", "abc", date1, date2));
        service.findCompetition(created.id());

        service.deleteCompetition(created.id());

        assertThrows(NotFoundException.class, () -> service.findCompetition(created.id()));
        assertEquals(0, service.findCompetitions().length);
        verify(listener, times(1)).added(created);
        verify(listener, times(1)).deleted(created);
        verifyNoMoreInteractions(listener);
    }

    @Test
    void deleteCompetitionWithWrongId() throws Exception {
        CompetitionDto created = service.create(new CreateCompetition("Alphabet", "abc", date1, date2));
        service.findCompetition(created.id());

        service.deleteCompetition(created.id() + "X");

        assertNotNull(service.findCompetition(created.id()));
        assertEquals(1, service.findCompetitions().length);
        verify(listener, times(1)).added(created);
        verifyNoMoreInteractions(listener);
    }

    @Test
    void tryToDeleteCompetitionWithNullAsId() {
        assertThrows(NullPointerException.class, () -> service.deleteCompetition(null));
    }

    @Test
    void queryUnknownCompetition() {
        assertThrows(NotFoundException.class, () -> service.findCompetition("unknown"));
    }

    @Test
    void findCompetitionsEmpty() {
        CompetitionDto[] competitions = service.findCompetitions();

        assertArrayEquals(new CompetitionDto[0], competitions);
    }

    @Test
    void findCompetitionsTwo() {
        assertEquals(0, service.findCompetitions().length);

        service.create(new CreateCompetition("Alphabet 1", "abc 1", date1, date1));
        service.create(new CreateCompetition("Alphabet 2", "abc 2", date2, date2));

        CompetitionDto[] competitions = service.findCompetitions();

        assertEquals(2, competitions.length);
        assertTrue(Arrays.stream(competitions).anyMatch(c -> c.name().equals("Alphabet 1")));
        assertTrue(Arrays.stream(competitions).anyMatch(c -> c.name().equals("Alphabet 2")));
    }

    @Test
    void registerListenerMoreThanOnceShouldHaveNoAdditionalEffect() {
        service.register(listener).register(listener);
        CompetitionDto created = service.create(new CreateCompetition("Alphabet", "abc", null, null));
        verify(listener, times(1)).added(created);
        verifyNoMoreInteractions(listener);
    }
}
