package org.lisasp.competition.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lisasp.competition.results.api.exception.NotFoundException;
import org.lisasp.competition.results.service.imports.ImportService;
import org.lisasp.competition.results.api.imports.exception.FileFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class ImportController {

    private final ImportService service;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Competition not found"),
            @ApiResponse(responseCode = "400", description = "The provided data could not be parsed"),
            @ApiResponse(responseCode = "204", description = "Import successful", useReturnTypeSchema = true)
    })
    @PutMapping("/import/{uploadId}/jauswertung")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void importFromJAuswertung(@PathVariable String uploadId, @RequestBody String competition) throws NotFoundException, FileFormatException {
        service.importFromJAuswertung(uploadId, competition);
    }
}

