package org.lisasp.results.imports.core;

import lombok.RequiredArgsConstructor;
import org.lisasp.results.competition.api.exception.NotFoundException;
import org.lisasp.results.imports.api.Competition;
import org.lisasp.results.imports.api.Entry;
import org.lisasp.results.imports.api.Event;
import org.lisasp.results.imports.api.exception.FileFormatException;
import org.lisasp.results.imports.jauswertung.JAuswertungImporter;
import org.lisasp.results.competition.model.CompetitionEntity;
import org.lisasp.results.competition.service.CompetitionService;
import org.lisasp.results.competition.model.EntryEntity;
import org.lisasp.results.competition.model.EventEntity;
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
    public void importCompetitionFromJAuswertung(String uploadId, String competitionJson) throws NotFoundException, FileFormatException {
        String id = competitionService.getCompetitionIdByUploadId(uploadId);

        Competition importedCompetition = jAuswertungImporter.importJson(competitionJson);

        store(id, importedCompetition);
    }

    private void store(String id, Competition competition) throws FileFormatException, NotFoundException {
        StorageResult result = storage.put(id, competition);
        if (result.equals(StorageResult.Unchanged)) {
            return;
        }

        competitionService.update(id, competitionUpdater -> {
            competitionUpdater.updateCompetition(competitionEntity -> updateCompetition(competition, competitionEntity));
            Arrays.stream(competition.getEvents()).forEach(event -> competitionUpdater.updateEvent(event.getEventType(), event.getAgegroup(), event.getGender(), event.getDiscipline(), event.getRound(), event.getInputValueType(), eventUpdater -> {
                eventUpdater.updateEvent(eventEntity -> updateEvent(event, eventEntity));
                Arrays.stream(event.getEntries()).forEach(entry -> eventUpdater.updateEntry(entry.getNumber(), e -> e.updateEntry(entryEntity -> updateEntry(entry, entryEntity))));
            }));
        });
    }

    private static void updateEntry(Entry entry, EntryEntity entryEntity) {
        entryEntity.setName(entry.getName());
        entryEntity.setClub(entry.getClub());
        entryEntity.setNationality(entry.getNationality());
        entryEntity.setPenalties(entry.getPenalties());
        entryEntity.setPlaceInHeat(entry.getPlaceInHeat());
        entryEntity.setStart(entry.getStart());
        entryEntity.setSwimmer(entry.getSwimmer());
        entryEntity.setTimeInMillis(entry.getTimeInMillis());
    }

    private static void updateEvent(Event event, EventEntity eventEntity) {
    }

    private static void updateCompetition(Competition competition, CompetitionEntity competitionEntity) {
        competitionEntity.setName(competition.getName());
        competitionEntity.setAcronym(competition.getAcronym());
        competitionEntity.setFrom(competition.getFrom());
        competitionEntity.setTill(competition.getTill());
    }
}