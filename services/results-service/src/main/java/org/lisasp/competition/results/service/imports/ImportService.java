package org.lisasp.competition.results.service.imports;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lisasp.competition.base.api.exception.FileFormatException;
import org.lisasp.competition.base.api.exception.InvalidDataException;
import org.lisasp.competition.base.api.exception.NotFoundException;
import org.lisasp.competition.base.api.exception.StorageException;
import org.lisasp.competition.results.api.imports.Competition;
import org.lisasp.competition.results.service.ResultService;
import org.lisasp.competition.results.service.imports.jauswertung.JAuswertungConverter;

@Slf4j
@RequiredArgsConstructor
public class ImportService {

    private final ObjectMapper mapper = new ObjectMapper();

    private final ResultService resultService;
    private final ImportStorage storage;

    private final JAuswertungConverter jAuswertungConverter = new JAuswertungConverter();

    public void importFromJAuswertung(String uploadId, int index, String competitionJson) throws NotFoundException, FileFormatException, InvalidDataException, StorageException {
        String id = resultService.getCompetitionIdByUploadId(uploadId);
        Competition importedCompetition = jAuswertungConverter.importJson(competitionJson);
        importCompetition(id, index, importedCompetition);
    }

    public void importCompetition(String uploadId, int index, String competitionJson) throws NotFoundException, FileFormatException, InvalidDataException, StorageException {
        try {
            String id = resultService.getCompetitionIdByUploadId(uploadId);
            importCompetition(id, index, mapper.readValue(competitionJson, Competition.class));
        } catch (JsonProcessingException e) {
            log.warn("Could not read json", e);
            throw new InvalidDataException("Could not read json");
        }
    }

    public void importCompetition(String id, int index, Competition competition) throws FileFormatException, NotFoundException, InvalidDataException, StorageException {
        StorageResult result = storage.put(id, index, competition);
        if (result == StorageResult.Unchanged) {
            return;
        }

        resultService.update(id, storage.get(id));
    }
}
