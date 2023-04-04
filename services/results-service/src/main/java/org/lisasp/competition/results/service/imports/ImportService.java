package org.lisasp.competition.results.service.imports;

import lombok.RequiredArgsConstructor;
import org.lisasp.competition.base.api.exception.FileFormatException;
import org.lisasp.competition.base.api.exception.InvalidDataException;
import org.lisasp.competition.base.api.exception.NotFoundException;
import org.lisasp.competition.results.api.imports.Competition;
import org.lisasp.competition.results.service.ResultService;
import org.lisasp.competition.results.service.imports.jauswertung.JAuswertungConverter;

@RequiredArgsConstructor
public class ImportService {
    private final ResultService resultService;
    private final ImportStorage storage;

    private final JAuswertungConverter jAuswertungConverter = new JAuswertungConverter();

    public void importFromJAuswertung(String uploadId, String competitionJson) throws NotFoundException, FileFormatException, InvalidDataException {
        String id = resultService.getCompetitionIdByUploadId(uploadId);
        Competition importedCompetition = jAuswertungConverter.importJson(competitionJson);
        importCompetition(id, importedCompetition);
    }

    public void importCompetition(String id, Competition competition) throws FileFormatException, NotFoundException, InvalidDataException {
        StorageResult result = storage.put(id, competition);
        if (result == StorageResult.Unchanged) {
            return;
        }

        resultService.update(id, competition);
    }
}
