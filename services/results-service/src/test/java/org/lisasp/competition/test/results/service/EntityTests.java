package org.lisasp.competition.test.results.service;

import org.junit.jupiter.api.Test;
import org.lisasp.competition.base.api.type.*;
import org.lisasp.competition.results.api.value.*;
import org.lisasp.competition.results.service.CompetitionResultEntity;
import org.lisasp.competition.results.service.EntryResultEntity;
import org.lisasp.competition.results.service.EventResultEntity;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EntityTests {
    @Test
    void competitionToString() {
        CompetitionResultEntity entity = new CompetitionResultEntity("Id-C");
        entity.setName("Name");
        entity.setAcronym("acr");
        entity.setUploadId("upId");
        entity.setFrom(LocalDate.of(2023, Month.JANUARY, 26));
        entity.setTill(LocalDate.of(2023, Month.JANUARY, 27));

        assertEquals("CompetitionResultEntity(id=Id-C, version=0, name=Name, acronym=acr, uploadId=upId,from=2023-01-26, till=2023-01-27)", entity.toString());
    }

    @Test
    void eventToString() {
        EventResultEntity entity = new EventResultEntity("Id-E");
        entity.setAgegroup("AK 12");
        entity.setGender(Gender.Male);
        entity.setDiscipline("100m Hindernis");
        entity.setEventType(EventType.Individual);
        entity.setInputValueType(InputValueType.Time);
        entity.setRound(new Round((byte) 1, RoundType.Final));

        assertEquals("EventResultEntity(id=Id-E, version=0, agegroup=AK 12, eventType=Individual, gender=Male, discipline=100m Hindernis, " +
                     "round=Round[round=1, type=Final], inputValueType=Time)", entity.toString());
    }

    @Test
    void entryToString() {
        EntryResultEntity entity = new EntryResultEntity("Id-Y");
        entity.setClub("Club");
        entity.setName("Name");
        entity.setNationality("Nat");
        entity.setNumber("123");
        entity.setPenalties(new Penalty[]{new Penalty("DSQ", PenaltyType.Disqualified)});
        entity.setPlaceInHeat(2);
        entity.setSplitTimes(new SplitTime[]{new SplitTime((byte) 0, 12345, (byte) 2)});
        entity.setStart(new Start("2", (byte) 3));
        entity.setSwimmer(new Swimmer[]{new Swimmer("123", "First", "Second", Sex.Female, (short) 2000)});

        assertEquals("EntryResultEntity(id=Id-Y, version=0, number=123, name=Name, club=Club, nationality=Nat, timeInMillis=0, placeInHeat=2, " +
                     "penalties=[Penalty[name=DSQ, type=Disqualified, points=0]], " +
                     "swimmer=[Swimmer[startNumber=123, firstName=First, lastName=Second, sex=Female, yearOfBirth=2000]], " +
                     "splitTimes=[SplitTime[position=0, timeInMillis=12345, placeInHeat=2]], start=Start[heat=2, lane=3])", entity.toString());
    }
}
