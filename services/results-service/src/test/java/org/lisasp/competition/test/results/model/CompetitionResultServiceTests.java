package org.lisasp.competition.test.results.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.lisasp.competition.results.api.*;
import org.lisasp.competition.base.api.exception.NotFoundException;
import org.lisasp.competition.base.api.type.EventType;
import org.lisasp.competition.base.api.type.Gender;
import org.lisasp.competition.base.api.type.InputValueType;
import org.lisasp.competition.base.api.type.RoundType;
import org.lisasp.competition.results.api.value.Round;
import org.lisasp.competition.results.service.CompetitionResultService;
import org.lisasp.competition.results.service.EntryResultService;
import org.lisasp.competition.results.service.EventResultService;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

@Tag("FastTest")
class CompetitionResultServiceTests {

    private CompetitionResultService service;
    private EventResultService eventResultService;
    private EntryResultService entryResultService;

    @BeforeEach
    void prepare() {
        TestDoubleEntryRepository entryRepository = new TestDoubleEntryRepository();
        TestDoubleEventRepository eventRepository = new TestDoubleEventRepository();
        TestDoubleCompetitionResultRepository competitionRepository = new TestDoubleCompetitionResultRepository();

        competitionRepository.setEventRepository(eventRepository);
        eventRepository.setCompetitionRepository(competitionRepository);
        eventRepository.setEntryRepository(entryRepository);
        entryRepository.setEventRepository(eventRepository);

        service = new CompetitionResultService(competitionRepository, eventRepository, entryRepository);
        eventResultService = new EventResultService(competitionRepository, eventRepository);
        entryResultService = new EntryResultService(eventRepository, entryRepository);
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

    @Nested
    class UpdateCompetition {

        @Test
        void updateCompetition() throws NotFoundException {
            CompetitionCreated competitionCreated = service.execute(new CreateCompetition("Alphabet 1", "abc 1", null, null));

            service.update(competitionCreated.id(), updater -> updater.updateCompetition(competition -> competition.setName("zyx")));

            CompetitionDto competitionDto = service.findCompetition(competitionCreated.id());
            assertEquals("zyx", competitionDto.getName());
        }

        @Test
        void addEventTest() throws NotFoundException {
            CompetitionCreated competitionCreated = service.execute(new CreateCompetition("Alphabet 1", "abc 1", null, null));
            String competitionId = competitionCreated.id();

            service.update(competitionId, updater -> {
                updater.updateCompetition(competition -> competition.setName("zyx"));
                updater.updateEvent(EventType.Individual,
                                    "AG 1",
                                    Gender.Female,
                                    "D1",
                                    new Round((byte) 0, RoundType.Final),
                                    InputValueType.Time,
                                    eventUpdater -> {
                                        eventUpdater.updateEvent(eventEntity -> {
                                        });
                                    });
            });

            CompetitionDto competitionDto = service.findCompetition(competitionId);
            assertEquals("zyx", competitionDto.getName());

            assertEquals(1, eventResultService.findEvents(competitionDto.getId()).length);
            EventDto event = eventResultService.findEvents(competitionDto.getId())[0];
            assertEquals("AG 1", event.getAgegroup());
            assertEquals(Gender.Female, event.getGender());
            assertEquals("D1", event.getDiscipline());
            assertEquals(new Round((byte) 0, RoundType.Final), event.getRound());
            assertEquals(InputValueType.Time, event.getInputValueType());
            assertEquals(0, entryResultService.findEntries(competitionId, event.getId()).length);
        }

        @Test
        void addTwoEventsTest() throws NotFoundException {
            CompetitionCreated competitionCreated = service.execute(new CreateCompetition("Alphabet 1", "abc 1", null, null));
            String competitionId = competitionCreated.id();

            service.update(competitionId, updater -> {
                updater.updateCompetition(competition -> competition.setName("zyx"));
                updater.updateEvent(EventType.Individual,
                                    "AG 1",
                                    Gender.Female,
                                    "D1",
                                    new Round((byte) 0, RoundType.Final),
                                    InputValueType.Time,
                                    eventUpdater -> {
                                        eventUpdater.updateEvent(eventEntity -> {
                                        });
                                    });
                updater.updateEvent(EventType.Individual, "AG 2", Gender.Male, "D2", new Round((byte) 1, RoundType.Heat), InputValueType.Rank, eventUpdater -> {
                    eventUpdater.updateEvent(eventEntity -> {
                    });
                });
            });

            CompetitionDto competitionDto = service.findCompetition(competitionId);
            assertEquals(competitionId, competitionDto.getId());
            assertEquals("zyx", competitionDto.getName());

            EventDto[] events =
                    Arrays.stream(eventResultService.findEvents(competitionDto.getId())).sorted(Comparator.comparing(EventDto::getAgegroup)).toArray(EventDto[]::new);
            assertEquals(2, events.length);

            EventDto event1 = events[0];
            assertEquals("AG 1", event1.getAgegroup());
            assertEquals(Gender.Female, event1.getGender());
            assertEquals("D1", event1.getDiscipline());
            assertEquals(new Round((byte) 0, RoundType.Final), event1.getRound());
            assertEquals(InputValueType.Time, event1.getInputValueType());
            assertEquals(0, entryResultService.findEntries(competitionId, event1.getId()).length);

            EventDto event2 = events[1];
            assertEquals("AG 2", event2.getAgegroup());
            assertEquals(Gender.Male, event2.getGender());
            assertEquals("D2", event2.getDiscipline());
            assertEquals(new Round((byte) 1, RoundType.Heat), event2.getRound());
            assertEquals(InputValueType.Rank, event2.getInputValueType());
            assertEquals(0, entryResultService.findEntries(competitionId, event2.getId()).length);
        }

        @Test
        void removeEventTest() throws NotFoundException {
            CompetitionCreated competitionCreated = service.execute(new CreateCompetition("Alphabet 1", "abc 1", null, null));
            service.update(competitionCreated.id(), updater -> {
                updater.updateCompetition(competition -> competition.setName("zyx"));
                updater.updateEvent(EventType.Individual,
                                    "AG 1",
                                    Gender.Female,
                                    "D1",
                                    new Round((byte) 0, RoundType.Final),
                                    InputValueType.Time,
                                    eventUpdater -> {
                                        eventUpdater.updateEvent(eventEntity -> {
                                        });
                                    });
            });

            service.update(competitionCreated.id(), updater -> {
                updater.updateCompetition(competition -> competition.setName("abc"));
            });

            CompetitionDto competitionDto = service.findCompetition(competitionCreated.id());
            assertEquals("abc", competitionDto.getName());
            assertEquals(0, eventResultService.findEvents(competitionDto.getId()).length);
        }

        @Test
        void removeOneOfTwoEventsTest() throws NotFoundException {
            CompetitionCreated competitionCreated = service.execute(new CreateCompetition("Alphabet 1", "abc 1", null, null));
            String competitionId = competitionCreated.id();
            service.update(competitionId, updater -> {
                updater.updateCompetition(competition -> competition.setName("zyx"));
                updater.updateEvent(EventType.Individual,
                                    "AG 1",
                                    Gender.Female,
                                    "D1",
                                    new Round((byte) 0, RoundType.Final),
                                    InputValueType.Time,
                                    eventUpdater -> {
                                        eventUpdater.updateEvent(eventEntity -> {
                                        });
                                    });
                updater.updateEvent(EventType.Individual, "AG 2", Gender.Male, "D2", new Round((byte) 1, RoundType.Heat), InputValueType.Rank, eventUpdater -> {
                    eventUpdater.updateEvent(eventEntity -> {
                    });
                });
            });

            service.update(competitionId, updater -> {
                updater.updateCompetition(competition -> competition.setName("zyx"));
                updater.updateEvent(EventType.Individual,
                                    "AG 1",
                                    Gender.Female,
                                    "D1",
                                    new Round((byte) 0, RoundType.Final),
                                    InputValueType.Time,
                                    eventUpdater -> {
                                        eventUpdater.updateEvent(eventEntity -> {
                                        });
                                    });
            });

            CompetitionDto competitionDto = service.findCompetition(competitionId);
            assertEquals("zyx", competitionDto.getName());

            assertEquals(1, eventResultService.findEvents(competitionDto.getId()).length);
            EventDto event = eventResultService.findEvents(competitionDto.getId())[0];
            assertEquals("AG 1", event.getAgegroup());
            assertEquals(Gender.Female, event.getGender());
            assertEquals("D1", event.getDiscipline());
            assertEquals(new Round((byte) 0, RoundType.Final), event.getRound());
            assertEquals(InputValueType.Time, event.getInputValueType());
            assertEquals(0, entryResultService.findEntries(competitionId, event.getId()).length);
        }

        @Test
        void addEntryTest() throws NotFoundException {
            CompetitionCreated competitionCreated = service.execute(new CreateCompetition("Alphabet 1", "abc 1", null, null));
            String competitionId = competitionCreated.id();

            service.update(competitionId, updater -> {
                updater.updateCompetition(competition -> competition.setName("zyx"));
                updater.updateEvent(EventType.Individual,
                                    "AG 1",
                                    Gender.Female,
                                    "D1",
                                    new Round((byte) 0, RoundType.Final),
                                    InputValueType.Time,
                                    eventUpdater -> {
                                        eventUpdater.updateEvent(eventEntity -> {
                                        });
                                        eventUpdater.updateEntry("123", entryUpdater -> {
                                            entryUpdater.updateEntry(entryEntity -> {
                                                entryEntity.setTimeInMillis(123450);
                                                entryEntity.setName("A");
                                                entryEntity.setClub("B");
                                                entryEntity.setNationality("GER");
                                            });
                                        });
                                    });
            });

            CompetitionDto competitionDto = service.findCompetition(competitionId);
            assertEquals("zyx", competitionDto.getName());

            assertEquals(1, eventResultService.findEvents(competitionDto.getId()).length);
            EventDto event = eventResultService.findEvents(competitionDto.getId())[0];

            assertEquals(1, entryResultService.findEntries(competitionId, event.getId()).length);
            EntryDto entry = entryResultService.findEntries(competitionId, event.getId())[0];
            assertEquals("123", entry.getNumber());
            assertEquals(123450, entry.getTimeInMillis());
            assertEquals("A", entry.getName());
            assertEquals("B", entry.getClub());
            assertEquals("GER", entry.getNationality());
        }

        @Test
        void removeEntryTest() throws NotFoundException {
            CompetitionCreated competitionCreated = service.execute(new CreateCompetition("Alphabet 1", "abc 1", null, null));
            String competitionId = competitionCreated.id();
            service.update(competitionId, updater -> {
                updater.updateCompetition(competition -> competition.setName("zyx"));
                updater.updateEvent(EventType.Individual,
                                    "AG 1",
                                    Gender.Female,
                                    "D1",
                                    new Round((byte) 0, RoundType.Final),
                                    InputValueType.Time,
                                    eventUpdater -> {
                                        eventUpdater.updateEvent(eventEntity -> {
                                        });
                                        eventUpdater.updateEntry("123", entryUpdater -> {
                                            entryUpdater.updateEntry(entryEntity -> {
                                                entryEntity.setTimeInMillis(123450);
                                                entryEntity.setName("A");
                                                entryEntity.setClub("B");
                                                entryEntity.setNationality("GER");
                                            });
                                        });
                                    });
            });

            service.update(competitionId, updater -> {
                updater.updateCompetition(competition -> competition.setName("zyx"));
                updater.updateEvent(EventType.Individual,
                                    "AG 1",
                                    Gender.Female,
                                    "D1",
                                    new Round((byte) 0, RoundType.Final),
                                    InputValueType.Time,
                                    eventUpdater -> {
                                        eventUpdater.updateEvent(eventEntity -> {
                                        });
                                    });
            });

            CompetitionDto competitionDto = service.findCompetition(competitionId);
            assertEquals("zyx", competitionDto.getName());

            assertEquals(1, eventResultService.findEvents(competitionDto.getId()).length);
            EventDto event = eventResultService.findEvents(competitionDto.getId())[0];

            assertEquals(0, entryResultService.findEntries(competitionId, event.getId()).length);
        }

        @Test
        void addTwoEntriesTest() throws NotFoundException {
            CompetitionCreated competitionCreated = service.execute(new CreateCompetition("Alphabet 1", "abc 1", null, null));
            String competitionId = competitionCreated.id();

            service.update(competitionId, updater -> {
                updater.updateCompetition(competition -> competition.setName("zyx"));
                updater.updateEvent(EventType.Individual,
                                    "AG 1",
                                    Gender.Female,
                                    "D1",
                                    new Round((byte) 0, RoundType.Final),
                                    InputValueType.Time,
                                    eventUpdater -> {
                                        eventUpdater.updateEvent(eventEntity -> {
                                        });
                                        eventUpdater.updateEntry("123", entryUpdater -> entryUpdater.updateEntry(entryEntity -> {
                                            entryEntity.setTimeInMillis(123450);
                                            entryEntity.setName("A");
                                            entryEntity.setClub("B");
                                            entryEntity.setNationality("GER");
                                        }));
                                        eventUpdater.updateEntry("456", entryUpdater -> {
                                            entryUpdater.updateEntry(entryEntity -> {
                                                entryEntity.setTimeInMillis(543210);
                                                entryEntity.setName("C");
                                                entryEntity.setClub("D");
                                                entryEntity.setNationality("GER");
                                            });
                                        });
                                    });
            });

            CompetitionDto competitionDto = service.findCompetition(competitionId);
            assertEquals("zyx", competitionDto.getName());

            assertEquals(1, eventResultService.findEvents(competitionDto.getId()).length);
            EventDto event = eventResultService.findEvents(competitionDto.getId())[0];

            EntryDto[] entries = Arrays.stream(entryResultService.findEntries(competitionId, event.getId()))
                                       .sorted(Comparator.comparing(EntryDto::getNumber))
                                       .toArray(EntryDto[]::new);
            assertEquals(2, entries.length);

            EntryDto entry1 = entries[0];
            assertEquals("123", entry1.getNumber());
            assertEquals(123450, entry1.getTimeInMillis());
            assertEquals("A", entry1.getName());
            assertEquals("B", entry1.getClub());
            assertEquals("GER", entry1.getNationality());

            EntryDto entry2 = entries[1];
            assertEquals("456", entry2.getNumber());
            assertEquals(543210, entry2.getTimeInMillis());
            assertEquals("C", entry2.getName());
            assertEquals("D", entry2.getClub());
            assertEquals("GER", entry2.getNationality());
        }

        @Test
        void removeOneOfTwoEntriesTest() throws NotFoundException {
            CompetitionCreated competitionCreated = service.execute(new CreateCompetition("Alphabet 1", "abc 1", null, null));
            String competitionId = competitionCreated.id();
            service.update(competitionId, updater -> {
                updater.updateCompetition(competition -> competition.setName("zyx"));
                updater.updateEvent(EventType.Individual,
                                    "AG 1",
                                    Gender.Female,
                                    "D1",
                                    new Round((byte) 0, RoundType.Final),
                                    InputValueType.Time,
                                    eventUpdater -> {
                                        eventUpdater.updateEvent(eventEntity -> {
                                        });
                                        eventUpdater.updateEntry("123", entryUpdater -> {
                                            entryUpdater.updateEntry(entryEntity -> {
                                                entryEntity.setTimeInMillis(123450);
                                                entryEntity.setName("A");
                                                entryEntity.setClub("B");
                                                entryEntity.setNationality("GER");
                                            });
                                        });
                                        eventUpdater.updateEntry("456", entryUpdater -> {
                                            entryUpdater.updateEntry(entryEntity -> {
                                                entryEntity.setTimeInMillis(543210);
                                                entryEntity.setName("C");
                                                entryEntity.setClub("D");
                                                entryEntity.setNationality("GER");
                                            });
                                        });
                                    });
            });

            service.update(competitionId, updater -> {
                updater.updateCompetition(competition -> competition.setName("zyx"));
                updater.updateEvent(EventType.Individual,
                                    "AG 1",
                                    Gender.Female,
                                    "D1",
                                    new Round((byte) 0, RoundType.Final),
                                    InputValueType.Time,
                                    eventUpdater -> {
                                        eventUpdater.updateEvent(eventEntity -> {
                                        });
                                        eventUpdater.updateEntry("123", entryUpdater -> {
                                            entryUpdater.updateEntry(entryEntity -> {
                                                entryEntity.setTimeInMillis(123450);
                                                entryEntity.setName("X");
                                                entryEntity.setClub("Y");
                                                entryEntity.setNationality("GER");
                                            });
                                        });
                                    });
            });

            CompetitionDto competitionDto = service.findCompetition(competitionId);
            assertEquals("zyx", competitionDto.getName());

            assertEquals(1, eventResultService.findEvents(competitionDto.getId()).length);
            EventDto event = eventResultService.findEvents(competitionDto.getId())[0];

            EntryDto[] entries = Arrays.stream(entryResultService.findEntries(competitionId, event.getId()))
                                       .sorted(Comparator.comparing(EntryDto::getNumber))
                                       .toArray(EntryDto[]::new);
            assertEquals(1, entries.length);

            EntryDto entry1 = entries[0];
            assertEquals("123", entry1.getNumber());
            assertEquals(123450, entry1.getTimeInMillis());
            assertEquals("X", entry1.getName());
            assertEquals("Y", entry1.getClub());
            assertEquals("GER", entry1.getNationality());
        }
    }
}
