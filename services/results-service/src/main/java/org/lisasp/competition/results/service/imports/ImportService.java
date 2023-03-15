package org.lisasp.competition.results.service.imports;

import lombok.RequiredArgsConstructor;
import org.lisasp.competition.base.api.exception.NotFoundException;
import org.lisasp.competition.base.api.exception.FileFormatException;
import org.lisasp.competition.results.api.imports.Competition;
import org.lisasp.competition.results.api.imports.Entry;
import org.lisasp.competition.results.api.imports.Event;
import org.lisasp.competition.results.service.CompetitionResultEntity;
import org.lisasp.competition.results.service.CompetitionResultService;
import org.lisasp.competition.results.service.EntryResultEntity;
import org.lisasp.competition.results.service.EventResultEntity;
import org.lisasp.competition.results.service.imports.jauswertung.JAuswertungImporter;

import java.util.Arrays;

@RequiredArgsConstructor
public class ImportService {
    private final CompetitionResultService competitionResultService;
    private final ImportStorage storage;

    private final JAuswertungImporter jAuswertungImporter = new JAuswertungImporter();

    public void importFromJAuswertung(String uploadId, String competitionJson) throws NotFoundException, FileFormatException {
        String id = competitionResultService.getCompetitionIdByUploadId(uploadId);

        Competition importedCompetition = jAuswertungImporter.importJson(competitionJson);

        store(id, importedCompetition);
    }

    private void store(String id, Competition competition) throws FileFormatException, NotFoundException {
        StorageResult result = storage.put(id, competition);
        if (result.equals(StorageResult.Unchanged)) {
            return;
        }

        competitionResultService.update(id, competitionUpdater -> {
            competitionUpdater.updateCompetition(competitionEntity -> updateCompetition(competition, competitionEntity));
            Arrays.stream(competition.getEvents())
                  .forEach(event -> competitionUpdater.updateEvent(event.getEventType(),
                                                                   event.getAgegroup(),
                                                                   event.getGender(),
                                                                   event.getDiscipline(),
                                                                   event.getRound(),
                                                                   event.getInputValueType(),
                                                                   eventUpdater -> {
                                                                       eventUpdater.updateEvent(eventEntity -> updateEvent(event, eventEntity));
                                                                       Arrays.stream(event.getEntries())
                                                                             .forEach(entry -> eventUpdater.updateEntry(entry.getNumber(),
                                                                                                                        e -> e.updateEntry(entryEntity -> updateEntry(
                                                                                                                                entry,
                                                                                                                                entryEntity))));
                                                                   }));
        });
    }

    private static void updateEntry(Entry entry, EntryResultEntity entryResultEntity) {
        entryResultEntity.setName(entry.getName());
        entryResultEntity.setClub(entry.getClub());
        entryResultEntity.setNationality(entry.getNationality());
        entryResultEntity.setPenalties(entry.getPenalties());
        entryResultEntity.setPlaceInHeat(entry.getPlaceInHeat());
        entryResultEntity.setStart(entry.getStart());
        entryResultEntity.setSwimmer(entry.getSwimmer());
        entryResultEntity.setSplitTimes(entry.getSplitTimes());
        entryResultEntity.setTimeInMillis(entry.getTimeInMillis());
    }

    private static void updateEvent(Event event, EventResultEntity eventResultEntity) {
    }

    private static void updateCompetition(Competition competition, CompetitionResultEntity competitionResultEntity) {
        competitionResultEntity.setName(competition.getName());
        competitionResultEntity.setAcronym(competition.getAcronym());
        competitionResultEntity.setFrom(competition.getFrom());
        competitionResultEntity.setTill(competition.getTill());
    }
}
