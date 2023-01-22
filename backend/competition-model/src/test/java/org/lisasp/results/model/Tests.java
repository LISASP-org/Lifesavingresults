package org.lisasp.results.model;

import org.junit.jupiter.api.Test;
import org.lisasp.results.api.dto.CompetitionCreated;
import org.lisasp.results.api.dto.CompetitionDto;
import org.lisasp.results.api.dto.CreateCompetition;
import org.lisasp.results.model.exception.NotFoundException;
import org.lisasp.results.model.competition.CompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class Tests {

    @Autowired
    private CompetitionService service;

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
    void queryUnknownCompetition() throws Exception {
        assertThrows(NotFoundException.class, () -> service.findCompetition("unkown"));
    }
}
