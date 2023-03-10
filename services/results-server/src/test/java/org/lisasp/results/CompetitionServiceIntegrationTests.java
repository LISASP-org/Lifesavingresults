package org.lisasp.results;

import org.junit.jupiter.api.*;
import org.lisasp.results.api.*;
import org.lisasp.results.api.type.EventType;
import org.lisasp.results.api.type.Gender;
import org.lisasp.results.api.type.InputValueType;
import org.lisasp.results.api.type.RoundType;
import org.lisasp.results.api.value.Round;
import org.lisasp.results.api.exception.NotFoundException;
import org.lisasp.results.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
@Tag("IntegrationTest")
@SpringBootTest
class CompetitionServiceIntegrationTests {

    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private CompetitionService service;
    @Autowired
    private EventService eventService;
    @Autowired
    private EntryService entryService;

    @Autowired
    private DatabaseCleaner cleaner;

    @BeforeEach
    void prepare() {
        cleaner = new DatabaseCleaner(competitionRepository, eventRepository, entryRepository);
    }

    @AfterEach
    void cleanup() {
        cleaner.clean();
    }

    @Test
    @Transactional
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
    @Transactional
    void queryUnknownCompetition() {
        assertThrows(NotFoundException.class, () -> service.findCompetition("unkown"));
    }

    @Test
    @Transactional
    void findCompetitionsEmpty() {
        CompetitionDto[] competitions = service.findCompetitions();

        assertArrayEquals(new CompetitionDto[0], competitions);
    }

    @Test
    @Transactional
    void findCompetitionsTwo() {
        service.execute(new CreateCompetition("Alphabet 1", "abc 1", null, null));
        service.execute(new CreateCompetition("Alphabet 2", "abc 2", null, null));

        CompetitionDto[] competitions = service.findCompetitions();

        assertEquals(2, competitions.length);
        assertTrue(Arrays.stream(competitions).anyMatch(c -> c.getName().equals("Alphabet 1")));
        assertTrue(Arrays.stream(competitions).anyMatch(c -> c.getName().equals("Alphabet 2")));
        assertTrue(Arrays.stream(competitions).allMatch(c -> c.getUploadId().equals("")));
    }

    @Test
    @Transactional
    void findCompetitionByUploadIdTest() throws NotFoundException {
        CompetitionCreated competitionCreated = service.execute(new CreateCompetition("Alphabet 1", "abc 1", null, null));
        CompetitionDto competitionDto = service.findCompetition(competitionCreated.id());

        String actual = service.getCompetitionIdByUploadId(competitionDto.getUploadId());

        assertEquals(competitionCreated.id(), actual);
    }

    @Nested
    class UpdateCompetition {

        @Test
        @Transactional
        void updateCompetition() throws NotFoundException {
            CompetitionCreated competitionCreated = service.execute(new CreateCompetition("Alphabet 1", "abc 1", null, null));

            service.update(competitionCreated.id(), updater -> updater.updateCompetition(competition -> competition.setName("zyx")));

            CompetitionDto competitionDto = service.findCompetition(competitionCreated.id());
            assertEquals("zyx", competitionDto.getName());
        }

        @Test
        @Transactional
        void addEventTest() throws NotFoundException {
            CompetitionCreated competitionCreated = service.execute(new CreateCompetition("Alphabet 1", "abc 1", null, null));
            String competitionId = competitionCreated.id();

            service.update(competitionId, updater -> {
                updater.updateCompetition(competition -> competition.setName("zyx"));
                updater.updateEvent(EventType.Individual, "AG 1", Gender.Female, "D1", new Round((byte) 0, RoundType.Final), InputValueType.Time, eventUpdater -> {
                    eventUpdater.updateEvent(eventEntity -> {
                    });
                });
            });

            CompetitionDto competitionDto = service.findCompetition(competitionId);
            assertEquals("zyx", competitionDto.getName());

            assertEquals(1, eventService.findEvents(competitionDto.getId()).length);
            EventDto event = eventService.findEvents(competitionDto.getId())[0];
            assertEquals("AG 1", event.getAgegroup());
            assertEquals(Gender.Female, event.getGender());
            assertEquals("D1", event.getDiscipline());
            assertEquals(new Round((byte) 0, RoundType.Final), event.getRound());
            assertEquals(InputValueType.Time, event.getInputValueType());
            assertEquals(0, entryService.findEntries(competitionId, event.getId()).length);
        }

        @Test
        @Transactional
        void addTwoEventsTest() throws NotFoundException {
            CompetitionCreated competitionCreated = service.execute(new CreateCompetition("Alphabet 1", "abc 1", null, null));
            String competitionId = competitionCreated.id();

            service.update(competitionId, updater -> {
                updater.updateCompetition(competition -> competition.setName("zyx"));
                updater.updateEvent(EventType.Individual, "AG 1", Gender.Female, "D1", new Round((byte) 0, RoundType.Final), InputValueType.Time, eventUpdater -> {
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

            EventDto[] events = Arrays.stream(eventService.findEvents(competitionDto.getId())).sorted(Comparator.comparing(EventDto::getAgegroup)).toArray(EventDto[]::new);
            assertEquals(2, events.length);

            EventDto event1 = events[0];
            assertEquals("AG 1", event1.getAgegroup());
            assertEquals(Gender.Female, event1.getGender());
            assertEquals("D1", event1.getDiscipline());
            assertEquals(new Round((byte) 0, RoundType.Final), event1.getRound());
            assertEquals(InputValueType.Time, event1.getInputValueType());
            assertEquals(0, entryService.findEntries(competitionId, event1.getId()).length);

            EventDto event2 = events[1];
            assertEquals("AG 2", event2.getAgegroup());
            assertEquals(Gender.Male, event2.getGender());
            assertEquals("D2", event2.getDiscipline());
            assertEquals(new Round((byte) 1, RoundType.Heat), event2.getRound());
            assertEquals(InputValueType.Rank, event2.getInputValueType());
            assertEquals(0, entryService.findEntries(competitionId, event2.getId()).length);
        }

        @Test
        @Transactional
        void removeEventTest() throws NotFoundException {
            CompetitionCreated competitionCreated = service.execute(new CreateCompetition("Alphabet 1", "abc 1", null, null));
            service.update(competitionCreated.id(), updater -> {
                updater.updateCompetition(competition -> competition.setName("zyx"));
                updater.updateEvent(EventType.Individual, "AG 1", Gender.Female, "D1", new Round((byte) 0, RoundType.Final), InputValueType.Time, eventUpdater -> {
                    eventUpdater.updateEvent(eventEntity -> {
                    });
                });
            });
            commit();

            service.update(competitionCreated.id(), updater -> {
                updater.updateCompetition(competition -> competition.setName("abc"));
            });

            CompetitionDto competitionDto = service.findCompetition(competitionCreated.id());
            assertEquals("abc", competitionDto.getName());
            assertEquals(0, eventService.findEvents(competitionDto.getId()).length);
        }

        @Test
        @Transactional
        void removeOneOfTwoEventsTest() throws NotFoundException {
            CompetitionCreated competitionCreated = service.execute(new CreateCompetition("Alphabet 1", "abc 1", null, null));
            String competitionId = competitionCreated.id();
            service.update(competitionId, updater -> {
                updater.updateCompetition(competition -> competition.setName("zyx"));
                updater.updateEvent(EventType.Individual, "AG 1", Gender.Female, "D1", new Round((byte) 0, RoundType.Final), InputValueType.Time, eventUpdater -> {
                    eventUpdater.updateEvent(eventEntity -> {
                    });
                });
                updater.updateEvent(EventType.Individual, "AG 2", Gender.Male, "D2", new Round((byte) 1, RoundType.Heat), InputValueType.Rank, eventUpdater -> {
                    eventUpdater.updateEvent(eventEntity -> {
                    });
                });
            });
            commit();

            service.update(competitionId, updater -> {
                updater.updateCompetition(competition -> competition.setName("zyx"));
                updater.updateEvent(EventType.Individual, "AG 1", Gender.Female, "D1", new Round((byte) 0, RoundType.Final), InputValueType.Time, eventUpdater -> {
                    eventUpdater.updateEvent(eventEntity -> {
                    });
                });
            });

            CompetitionDto competitionDto = service.findCompetition(competitionId);
            assertEquals("zyx", competitionDto.getName());

            assertEquals(1, eventService.findEvents(competitionDto.getId()).length);
            EventDto event = eventService.findEvents(competitionDto.getId())[0];
            assertEquals("AG 1", event.getAgegroup());
            assertEquals(Gender.Female, event.getGender());
            assertEquals("D1", event.getDiscipline());
            assertEquals(new Round((byte) 0, RoundType.Final), event.getRound());
            assertEquals(InputValueType.Time, event.getInputValueType());
            assertEquals(0, entryService.findEntries(competitionId, event.getId()).length);
        }

        @Test
        @Transactional
        void addEntryTest() throws NotFoundException {
            CompetitionCreated competitionCreated = service.execute(new CreateCompetition("Alphabet 1", "abc 1", null, null));
            String competitionId = competitionCreated.id();

            service.update(competitionId, updater -> {
                updater.updateCompetition(competition -> competition.setName("zyx"));
                updater.updateEvent(EventType.Individual, "AG 1", Gender.Female, "D1", new Round((byte) 0, RoundType.Final), InputValueType.Time, eventUpdater -> {
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

            assertEquals(1, eventService.findEvents(competitionDto.getId()).length);
            EventDto event = eventService.findEvents(competitionDto.getId())[0];

            assertEquals(1, entryService.findEntries(competitionId, event.getId()).length);
            EntryDto entry = entryService.findEntries(competitionId, event.getId())[0];
            assertEquals("123", entry.getNumber());
            assertEquals(123450, entry.getTimeInMillis());
            assertEquals("A", entry.getName());
            assertEquals("B", entry.getClub());
            assertEquals("GER", entry.getNationality());
        }

        @Test
        @Transactional
        void removeEntryTest() throws NotFoundException {
            CompetitionCreated competitionCreated = service.execute(new CreateCompetition("Alphabet 1", "abc 1", null, null));
            String competitionId = competitionCreated.id();
            service.update(competitionId, updater -> {
                updater.updateCompetition(competition -> competition.setName("zyx"));
                updater.updateEvent(EventType.Individual, "AG 1", Gender.Female, "D1", new Round((byte) 0, RoundType.Final), InputValueType.Time, eventUpdater -> {
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
            commit();

            service.update(competitionId, updater -> {
                updater.updateCompetition(competition -> competition.setName("zyx"));
                updater.updateEvent(EventType.Individual, "AG 1", Gender.Female, "D1", new Round((byte) 0, RoundType.Final), InputValueType.Time, eventUpdater -> {
                    eventUpdater.updateEvent(eventEntity -> {
                    });
                });
            });

            CompetitionDto competitionDto = service.findCompetition(competitionId);
            assertEquals("zyx", competitionDto.getName());

            assertEquals(1, eventService.findEvents(competitionDto.getId()).length);
            EventDto event = eventService.findEvents(competitionDto.getId())[0];

            assertEquals(0, entryService.findEntries(competitionId, event.getId()).length);
        }

        @Test
        @Transactional
        void addTwoEntriesTest() throws NotFoundException {
            CompetitionCreated competitionCreated = service.execute(new CreateCompetition("Alphabet 1", "abc 1", null, null));
            String competitionId = competitionCreated.id();

            service.update(competitionId, updater -> {
                updater.updateCompetition(competition -> competition.setName("zyx"));
                updater.updateEvent(EventType.Individual, "AG 1", Gender.Female, "D1", new Round((byte) 0, RoundType.Final), InputValueType.Time, eventUpdater -> {
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

            assertEquals(1, eventService.findEvents(competitionDto.getId()).length);
            EventDto event = eventService.findEvents(competitionDto.getId())[0];

            EntryDto[] entries = Arrays.stream(entryService.findEntries(competitionId, event.getId())).sorted(Comparator.comparing(EntryDto::getNumber)).toArray(EntryDto[]::new);
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
        @Transactional
        void removeOneOfTwoEntriesTest() throws NotFoundException {
            CompetitionCreated competitionCreated = service.execute(new CreateCompetition("Alphabet 1", "abc 1", null, null));
            String competitionId = competitionCreated.id();
            service.update(competitionId, updater -> {
                updater.updateCompetition(competition -> competition.setName("zyx"));
                updater.updateEvent(EventType.Individual, "AG 1", Gender.Female, "D1", new Round((byte) 0, RoundType.Final), InputValueType.Time, eventUpdater -> {
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
            commit();

            service.update(competitionId, updater -> {
                updater.updateCompetition(competition -> competition.setName("zyx"));
                updater.updateEvent(EventType.Individual, "AG 1", Gender.Female, "D1", new Round((byte) 0, RoundType.Final), InputValueType.Time, eventUpdater -> {
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

            assertEquals(1, eventService.findEvents(competitionDto.getId()).length);
            EventDto event = eventService.findEvents(competitionDto.getId())[0];

            EntryDto[] entries = Arrays.stream(entryService.findEntries(competitionId, event.getId())).sorted(Comparator.comparing(EntryDto::getNumber)).toArray(EntryDto[]::new);
            assertEquals(1, entries.length);

            EntryDto entry1 = entries[0];
            assertEquals("123", entry1.getNumber());
            assertEquals(123450, entry1.getTimeInMillis());
            assertEquals("X", entry1.getName());
            assertEquals("Y", entry1.getClub());
            assertEquals("GER", entry1.getNationality());
        }
    }

    private static void commit() {
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();
    }
}
