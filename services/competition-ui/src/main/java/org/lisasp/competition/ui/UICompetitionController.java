package org.lisasp.competition.ui;

import lombok.extern.slf4j.Slf4j;
import org.lisasp.competition.api.CompetitionDto;
import org.lisasp.competition.api.CreateCompetition;
import org.lisasp.competition.base.api.exception.NotFoundException;
import org.lisasp.competition.service.CompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class UICompetitionController {

    @Autowired
    private CompetitionService competitionService;

    @GetMapping({"/", "/competitions", "/competitions/", "/web/competitions", "/web/competitions/"})
    public String findCompetitions(Model model) {
        model.addAttribute("title", "Wettkämpfe");

        CompetitionDto[] competitions = competitionService.findCompetitions();

        model.addAttribute("competitions", competitions);
        return "competitions";
    }

    @GetMapping({"/web/competitions/new", "/web/competitions/new/"})
    public String createCompetition(Model model) {
        model.addAttribute("title", "Wettkampf anlegen");
        model.addAttribute("competition", new CreateCompetition());
        return "competitions-new";
    }

    @PostMapping({"/web/competitions/new", "/web/competitions/new/"})
    public String createCompetition(Model model,@ModelAttribute("competition") CreateCompetition newCompetition) {
        model.addAttribute("title", "Wettkampf anlegen");

        CompetitionDto competition = competitionService.create(newCompetition);

        return "redirect:/web/competition/" + competition.id();
    }

    @GetMapping({"/web/competition/{id}", "/web/competition/{id}/"})
    public String getCompetition(Model model, @PathVariable("id") String id) throws NotFoundException {
        model.addAttribute("title", "Wettkampf");

        CompetitionDto competition = competitionService.findCompetition(id);

        if (competition != null) {
            model.addAttribute("competition", competition);
        } else {
            model.addAttribute("error", String.format("Wettkampf '%s' nicht gefunden.", id));
        }
        return "competition";
    }
}
