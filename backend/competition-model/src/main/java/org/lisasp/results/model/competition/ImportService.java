package org.lisasp.results.model.competition;

import lombok.RequiredArgsConstructor;
import org.lisasp.results.api.Competition;
import org.lisasp.results.imports.jauswertung.JAuswertungImporter;
import org.lisasp.results.model.exception.NotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ImportService {
    private final CompetitionRepository repository;

    private final JAuswertungImporter jAuswertungImporter = new JAuswertungImporter();

    public void importFromJAuswertumg(String id, org.lisasp.results.imports.jauswertung.model.Competition competitionToImport) throws NotFoundException {
        CompetitionEntity competition = repository.findById(id).orElseThrow(() -> new NotFoundException("Competition", id));
        Competition importedCompetition = jAuswertungImporter.importCompetition(competitionToImport);

        store(id, importedCompetition);
    }

    private void store(String id, org.lisasp.results.api.Competition competition) throws NotFoundException {
        CompetitionEntity entity = repository.findById(id).orElseThrow(() -> new NotFoundException("Competition", id));
        entity.setName(competition.getName());
        entity.setAcronym(competition.getAcronym());
        entity.setFrom(competition.getFrom());
        entity.setTill(competition.getTill());

        repository.save(entity);
    }
}
