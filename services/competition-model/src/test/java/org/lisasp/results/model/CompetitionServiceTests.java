package org.lisasp.results.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lisasp.competition.api.dto.CompetitionCreated;
import org.lisasp.competition.api.dto.CompetitionDto;
import org.lisasp.competition.api.dto.CreateCompetition;
import org.lisasp.competition.api.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CompetitionServiceTests {


    @Autowired
    private DatabaseCleaner cleaner;

    @Autowired
    private CompetitionService service;

    @BeforeEach
    void prepare() {
        cleaner.clean();
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
        service.execute(new CreateCompetition("Alphabet 1", "abc 1", null, null));
        service.execute(new CreateCompetition("Alphabet 2", "abc 2", null, null));

        CompetitionDto[] competitions = service.findCompetitions();

        assertEquals(2, competitions.length);
        assertTrue(Arrays.stream(competitions).anyMatch(c -> c.getName().equals("Alphabet 1")));
        assertTrue(Arrays.stream(competitions).anyMatch(c -> c.getName().equals("Alphabet 2")));
        assertTrue(Arrays.stream(competitions).allMatch(c -> c.getUploadId().equals("")));
    }
}
