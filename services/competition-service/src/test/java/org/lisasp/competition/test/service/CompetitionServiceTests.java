package org.lisasp.competition.test.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.lisasp.competition.api.CompetitionCreated;
import org.lisasp.competition.api.CompetitionDto;
import org.lisasp.competition.api.CreateCompetition;
import org.lisasp.competition.base.api.exception.NotFoundException;
import org.lisasp.competition.service.CompetitionService;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@Tag("FastTest")
class CompetitionServiceTests {

    private CompetitionService service;

    @BeforeEach
    void prepare() {
        TestDoubleCompetitionRepository competitionRepository = new TestDoubleCompetitionRepository();

        service = new CompetitionService(competitionRepository);
    }

    @Test
    void createAndQueryCompetition() throws Exception {
        CompetitionCreated created = service.execute(new CreateCompetition("Alphabet", "abc", null, null));
        assertNotNull(created);
        assertNotNull(created.id());

        CompetitionDto competitionDto = service.findCompetition(created.id());

        assertNotNull(competitionDto);
        assertEquals("Alphabet", competitionDto.getName());
        assertEquals("abc", competitionDto.getAcronym());
    }

    @Test
    void queryUnknownCompetition() {
        assertThrows(NotFoundException.class, () -> service.findCompetition("unkown"));
    }

    @Test
    void findCompetitionsEmpty() {
        CompetitionDto[] competitions = service.findCompetitions();

        assertArrayEquals(new CompetitionDto[0], competitions);
    }

    @Test
    void findCompetitionsTwo() {
        assertEquals(0, service.findCompetitions().length);

        service.execute(new CreateCompetition("Alphabet 1", "abc 1", null, null));
        service.execute(new CreateCompetition("Alphabet 2", "abc 2", null, null));

        CompetitionDto[] competitions = service.findCompetitions();

        assertEquals(2, competitions.length);
        assertTrue(Arrays.stream(competitions).anyMatch(c -> c.getName().equals("Alphabet 1")));
        assertTrue(Arrays.stream(competitions).anyMatch(c -> c.getName().equals("Alphabet 2")));
        assertTrue(Arrays.stream(competitions).allMatch(c -> c.getUploadId().equals("")));
    }

    @Test
    void findCompetitionByUploadIdTest() throws NotFoundException {
        CompetitionCreated competitionCreated = service.execute(new CreateCompetition("Alphabet 1", "abc 1", null, null));
        CompetitionDto competitionDto = service.findCompetition(competitionCreated.id());

        String actual = service.getCompetitionIdByUploadId(competitionDto.getUploadId());

        assertEquals(competitionCreated.id(), actual);
    }
}
