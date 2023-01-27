package org.lisasp.results.imports.core;

import lombok.RequiredArgsConstructor;
import org.lisasp.competition.api.exception.NotFoundException;
import org.lisasp.results.imports.api.Competition;
import org.lisasp.results.imports.jauswertung.FileFormatException;
import org.lisasp.results.imports.jauswertung.JAuswertungImporter;
import org.lisasp.results.model.CompetitionService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@RequiredArgsConstructor
@Component
public class ImportService {
    private final CompetitionService competitionService;
    private final ImportStorage storage;

    private final JAuswertungImporter jAuswertungImporter = new JAuswertungImporter();

    @Transactional
    public void importFromJAuswertung(String uploadId, org.lisasp.results.imports.jauswertung.model.Competition competitionToImport) throws NotFoundException, FileFormatException {
        String id = competitionService.getCompetitionIdByUploadId(uploadId);

        Competition importedCompetition = jAuswertungImporter.importCompetition(competitionToImport);

        store(id, importedCompetition);
    }

    private void store(String id, Competition competition) throws FileFormatException, NotFoundException {
        storage.put(id, competition);

        competitionService.update(id, competitionUpdater -> {
            competitionUpdater.updateCompetition(competitionEntity -> {
                competitionEntity.setName(competition.getName());
                competitionEntity.setAcronym(competition.getAcronym());
                competitionEntity.setFrom(competition.getFrom());
                competitionEntity.setTill(competition.getTill());
            });
            Arrays.stream(competition.getEvents()).forEach(event -> competitionUpdater.updateEvent(event.getEventType(), event.getAgegroup(), event.getGender(), event.getDiscipline(), event.getRound(), event.getInputValueType(), eventUpdater -> {
                eventUpdater.updateEvent(eventEntity -> {
                    eventEntity.setEventType(event.getEventType());
                    eventEntity.setAgegroup(event.getAgegroup());
                    eventEntity.setGender(event.getGender());
                    eventEntity.setDiscipline(event.getDiscipline());
                    eventEntity.setRound(event.getRound());
                    eventEntity.setInputValueType(event.getInputValueType());
                });
                Arrays.stream(event.getEntries()).forEach(entry -> eventUpdater.updateEntry(entry.getNumber(), e -> {
                    e.updateEntry(entryEntity -> {
                        entryEntity.setNumber(entry.getNumber());
                        entryEntity.setName(entry.getName());
                        entryEntity.setClub(entry.getClub());
                        entryEntity.setNationality(entry.getNationality());
                        entryEntity.setPenalties(entry.getPenalties());
                        entryEntity.setPlaceInHeat(entry.getPlaceInHeat());
                        entryEntity.setStart(entry.getStart());
                        entryEntity.setSwimmer(entry.getSwimmer());
                        entryEntity.setTimeInMillis(entry.getTimeInMillis());
                    });
                }));
            }));
        });
    }
}
