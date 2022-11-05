package dev.drugowick.timeseriespoc.controller;

import dev.drugowick.timeseriespoc.domain.repository.CustomSQLRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminPageController extends BaseController {

    // TODO Bypassing service layer for now
    private final CustomSQLRepository customSQLRepository;

    public AdminPageController(CustomSQLRepository customSQLRepository) {
        this.customSQLRepository = customSQLRepository;
    }

    @RequestMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("userMeasurements", customSQLRepository.countByUsername());
        return "admin";
    }
}
