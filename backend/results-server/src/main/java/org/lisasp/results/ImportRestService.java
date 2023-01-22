package org.lisasp.results;

import lombok.RequiredArgsConstructor;
import org.lisasp.results.imports.jauswertung.model.Competition;
import org.lisasp.results.model.competition.CompetitionEntity;
import org.lisasp.results.model.competition.CompetitionService;
import org.lisasp.results.model.competition.ImportService;
import org.lisasp.results.model.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class ImportRestService {

    private final ImportService service;

    @PostMapping("/import/jauswertung/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void importFromJAuswertung(@PathVariable String id, @RequestBody Competition competition) throws NotFoundException {
        service.importFromJAuswertumg(id, competition);
    }
}

