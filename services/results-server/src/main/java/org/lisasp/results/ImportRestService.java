package org.lisasp.results;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lisasp.competition.api.exception.NotFoundException;
import org.lisasp.results.imports.core.ImportService;
import org.lisasp.results.imports.jauswertung.FileFormatException;
import org.lisasp.results.imports.jauswertung.model.Competition;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class ImportRestService {

    private final ImportService service;

    @PostMapping("/import/jauswertung/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void importFromJAuswertung(@PathVariable String id, @RequestBody Competition competition) throws NotFoundException, FileFormatException {
        log.info("Import from JAuswertung for {}", competition.getName());
        service.importFromJAuswertung(id, competition);
    }
}

