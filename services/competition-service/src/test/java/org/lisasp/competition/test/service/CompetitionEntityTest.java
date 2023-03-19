package org.lisasp.competition.test.service;

import org.junit.jupiter.api.Test;
import org.lisasp.competition.service.CompetitionEntity;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CompetitionEntityTest {
    @Test
    void toStringTest() {
        CompetitionEntity entity = new CompetitionEntity("id1");
        entity.setName("Name");
        entity.setAcronym("acr");
        entity.setFrom(LocalDate.of(2023, Month.FEBRUARY, 12));
        entity.setTill(LocalDate.of(2023, Month.FEBRUARY, 13));

        assertEquals("CompetitionEntity(id=id1, version=0, name=Name, acronym=acr, from=2023-02-12, till=2023-02-13)", entity.toString());
    }
}
