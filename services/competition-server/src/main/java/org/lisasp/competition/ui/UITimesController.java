package org.lisasp.competition.ui;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lisasp.competition.base.api.type.Gender;
import org.lisasp.competition.times.service.TimesService;
import org.lisasp.competition.times.api.TimeDto;
import org.lisasp.competition.times.api.TimesQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UITimesController {

    private final TimesService timesService;

    @GetMapping({"/web/times", "web/times/"})
    public String findCompetitions(Model model, TimesQuery query) {
        model.addAttribute("title", "Zeiten");

        if (query == null) {
            query = new TimesQuery();
        }
        if (query.getAgegroup() == null) {
            query.setAgegroup("");
        }
        if (query.getGender() == null) {
            query.setGender(Gender.Unknown);
        }
        if (query.getDiscipline() == null) {
            query.setDiscipline("");
        }

        TimeDto[] times = timesService.findTimes(query);
        if (times == null) {
            times = new TimeDto[0];
        }
        if (times.length == 0) {
            log.info("No times found for '{}'", query);
        }

        model.addAttribute("query", query);
        model.addAttribute("times", times);
        return "times/times";
    }
}
