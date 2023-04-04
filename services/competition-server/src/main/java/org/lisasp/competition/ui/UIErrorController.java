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
public class UIErrorController {

    @Autowired
    private CompetitionService competitionService;

    @GetMapping({"/error",})
    public String findCompetitions(Model model) {
        model.addAttribute("title", "Fehler");

        return "error";
    }
}
