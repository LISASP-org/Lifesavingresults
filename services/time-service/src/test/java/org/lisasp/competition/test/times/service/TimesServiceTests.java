package org.lisasp.competition.test.times.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lisasp.competition.base.api.ChangeType;
import org.lisasp.competition.base.api.type.EventType;
import org.lisasp.competition.base.api.type.Gender;
import org.lisasp.competition.base.api.type.InputValueType;
import org.lisasp.competition.base.api.type.RoundType;
import org.lisasp.competition.results.api.CompetitionDto;
import org.lisasp.competition.results.api.EntryChangedEvent;
import org.lisasp.competition.results.api.EntryDto;
import org.lisasp.competition.results.api.EventDto;
import org.lisasp.competition.results.api.value.Penalty;
import org.lisasp.competition.results.api.value.Round;
import org.lisasp.competition.results.api.value.Start;
import org.lisasp.competition.results.api.value.Swimmer;
import org.lisasp.competition.times.api.TimeDto;
import org.lisasp.competition.times.api.TimesQuery;
import org.lisasp.competition.times.service.TimesService;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimesServiceTests {

    private static final CompetitionDto competition =
            new CompetitionDto("1", 1, "Name", "Acr", LocalDate.of(2023, Month.JULY, 17), LocalDate.of(2023, Month.JULY, 17));
    private static final EventDto event =
            new EventDto("2",
                         1,
                         "AK 13/14",
                         EventType.Individual,
                         Gender.Male,
                         "100m Hindernisschwimmen",
                         new Round((byte) 1, RoundType.Final),
                         InputValueType.Time);
    private static final EventDto eventInvalid =
            new EventDto("2", 1, "AK 13/14", EventType.Individual, Gender.Male, "Invalid", new Round((byte) 1, RoundType.Final), InputValueType.Time);
    private static final EntryDto entry =
            new EntryDto("3", 1, "12", "Schwimmer", "Klub", "GER", 12345, 0, new Penalty[0], new Swimmer[0], new Start("1", (byte) 1));

    private TimesService service;

    @BeforeEach
    void prepare() {
        service = new TimesService(new TestTimesRepository());
    }

    @Test
    void receiveValidTime() {
        service.receive(new EntryChangedEvent(ChangeType.Created, competition, event, entry));

        TimeDto[] times = service.findTimes(new TimesQuery(EventType.Individual, "AK 13/14", Gender.Male, "100m Hindernisschwimmen"));

        assertEquals(1, times.length);
        assertEquals(new TimeDto("3",
                                 "Name",
                                 "Acr",
                                 EventType.Individual,
                                 "Schwimmer",
                                 "Klub",
                                 "GER",
                                 "AK 13/14",
                                 Gender.Male,
                                 "100m Hindernisschwimmen",
                                 12345),
                     times[0]);
    }

    @Test
    void receiveValidTimeTwice() {
        service.receive(new EntryChangedEvent(ChangeType.Created, competition, event, entry));
        service.receive(new EntryChangedEvent(ChangeType.Updated, competition, event, entry));

        TimeDto[] times = service.findTimes(new TimesQuery(EventType.Individual, "AK 13/14", Gender.Male, "100m Hindernisschwimmen"));

        assertEquals(1, times.length);
        assertEquals(new TimeDto("3",
                                 "Name",
                                 "Acr",
                                 EventType.Individual,
                                 "Schwimmer",
                                 "Klub",
                                 "GER",
                                 "AK 13/14",
                                 Gender.Male,
                                 "100m Hindernisschwimmen",
                                 12345),
                     times[0]);
    }

    @Test
    void removedValidTime() {
        service.receive(new EntryChangedEvent(ChangeType.Created, competition, event, entry));
        service.receive(new EntryChangedEvent(ChangeType.Deleted, competition, event, entry));

        TimeDto[] times = service.findTimes(new TimesQuery(EventType.Individual, "AK 13/14", Gender.Male, "100m Hindernisschwimmen"));

        assertEquals(0, times.length);
    }

    @Test
    void ignoreInvalidTime() {
        service.receive(new EntryChangedEvent(ChangeType.Created, competition, eventInvalid, entry));

        TimeDto[] times = service.findTimes(new TimesQuery(EventType.Individual, "AK 13/14", Gender.Male, "Invalid"));

        assertEquals(0, times.length);
    }

    @Test
    void rescue2022MasterExample() {
        TimeDto expected = new TimeDto(
                "a2e0253f-35b1-4497-aaee-06a479a8ae2b",
                "Rescue 2022",
                "Rescue2022",
                EventType.Team,
                "Gouda Reddingsbrigade",
                "Gouda Reddingsbrigade",
                "NED",
                "AK 140",
                Gender.Male,
                "4*50m Hindernisstaffel",
                153610);
        EntryChangedEvent event = new EntryChangedEvent(
                ChangeType.Created,
                new CompetitionDto("88be3d31-d2b4-48de-8baa-f1cfaf8925bf",
                                   1,
                                   "Rescue 2022",
                                   "Rescue2022",
                                   LocalDate.of(2022, Month.SEPTEMBER, 30),
                                   LocalDate.of(2022, Month.SEPTEMBER, 30)),
                new EventDto("ba14373e-854a-44fc-b33c-1495aeac1057", 1, "M140-169", EventType.Team, Gender.Male,
                             "4x50 Obstacle Relay", new Round((byte) 7, RoundType.Heat), InputValueType.Time),
                new EntryDto("a2e0253f-35b1-4497-aaee-06a479a8ae2b",
                             1,
                             "5512",
                             "Gouda Reddingsbrigade",
                             "Gouda Reddingsbrigade",
                             "NED",
                             153610,
                             0,
                             new Penalty[0],
                             new Swimmer[0],
                             new Start("3", (byte) 7)));
        service.receive(event);

        TimeDto[] times = service.findTimes(new TimesQuery(EventType.Team, "AK 140", Gender.Male, "4*50m Hindernisstaffel"));

        assertEquals(1, times.length);
        assertEquals(expected, times[0]);
    }
}
