package org.lisasp.competition.ui;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class UIErrorController implements ErrorController {

    @GetMapping({"/error",})
    public String handleError(Model model, HttpServletRequest request) {
        model.addAttribute("title", "Fehler");
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            model.addAttribute("statuscode", statusCode);
        }
        return "errorpage";
    }
}
