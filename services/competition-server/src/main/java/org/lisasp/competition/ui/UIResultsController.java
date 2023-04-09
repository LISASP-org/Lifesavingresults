package org.lisasp.competition.ui;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lisasp.competition.base.api.exception.FileFormatException;
import org.lisasp.competition.base.api.exception.InvalidDataException;
import org.lisasp.competition.base.api.exception.NotFoundException;
import org.lisasp.competition.results.api.CompetitionDto;
import org.lisasp.competition.results.api.EntryDto;
import org.lisasp.competition.results.api.EventDto;
import org.lisasp.competition.results.service.ResultService;
import org.lisasp.competition.results.service.imports.ImportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UIResultsController {

    private final ResultService resultService;

    private final ImportService importService;


    @GetMapping({"/web/results", "web/results/competitions", "/web/results/", "/web/results/competitions/"})
    public String findCompetitions(Model model) {
        model.addAttribute("title", "Wettkämpfe");

        CompetitionDto[] competitions = resultService.findCompetitions();

        model.addAttribute("competitions", competitions);
        return "results/competitions";
    }

    @GetMapping({"/web/results/competition/{id}", "/web/results/competition/{id}/"})
    public String getCompetition(Model model, @PathVariable("id") String id) throws NotFoundException {
        model.addAttribute("title", "Wettkampf");

        CompetitionDto competition = resultService.findCompetition(id);

        if (competition != null) {
            model.addAttribute("competition", competition);
            model.addAttribute("events", Arrays.stream(resultService.findEvents(competition.id())).sorted((a, b) -> a.agegroup().compareTo(b.agegroup())).toArray(EventDto[]::new));
        } else {
            model.addAttribute("error", String.format("Wettkampf '%s' nicht gefunden.", id));
            model.addAttribute("events", new EventDto[0]);
        }
        return "results/events";
    }

    @GetMapping({"/web/results/event/{id}", "/web/results/event/{id}/"})
    public String getEvent(Model model, @PathVariable("id") String id) throws NotFoundException {
        model.addAttribute("title", "Wettkampf");

        EventDto event = resultService.findEvent(id);

        if (event != null) {
            model.addAttribute("event", event);
            model.addAttribute("entries", Arrays.stream(resultService.findEntries(id)).sorted((a,b) -> compareEntries(a, b)).toArray(EntryDto[]::new));
        } else {
            model.addAttribute("error", String.format("Event '%s' nicht gefunden.", id));
            model.addAttribute("entries", new EntryDto[0]);
        }
        return "results/entries";
    }

    private static int compareEntries(EntryDto a, EntryDto b) {
        if (a.penalties().length == 0 && b.penalties().length > 0) {
            return -1;
        }
        if (a.penalties().length > 0 && b.penalties().length == 0) {
            return 1;
        }
        if (a.penalties().length > 0 && b.penalties().length > 0) {
            return 0;
        }
        return a.timeInMillis() - b.timeInMillis();
    }

    @GetMapping({"/web/results/upload/competition/{id}", "/web/results/upload/competition/{id}"})
    public String uploadResult(Model model, @PathVariable("id") String id) throws NotFoundException {
        model.addAttribute("title", "Daten importieren");

        CompetitionDto competition = resultService.findCompetition(id);

        if (competition != null) {
            model.addAttribute("competition", competition);
        } else {
            model.addAttribute("error", String.format("Event '%s' nicht gefunden.", id));
        }
        return "results/upload";
    }

    @PostMapping("/web/results/upload/{uploadId}")
    public String uploadResult(@RequestParam("file") MultipartFile file, @PathVariable("uploadId") String id,
                               RedirectAttributes redirectAttributes) throws InvalidDataException, NotFoundException, FileFormatException {
        try {
            importService.importCompetition(id, new String(file.getBytes(), StandardCharsets.UTF_8));
            String competitionId = resultService.getCompetitionIdByUploadId(id);

            redirectAttributes.addFlashAttribute("message",
                                                 "Die Datei '" + file.getOriginalFilename() + "' wurde erfolgreich importiert");
            return "redirect:/web/results/competition/" + competitionId;
        } catch (IOException ex) {
            log.info("Could not read upload", ex);
            throw new InvalidDataException("Could not read upload");
        }
    }
}
