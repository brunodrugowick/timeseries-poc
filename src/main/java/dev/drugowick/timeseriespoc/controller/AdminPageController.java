package dev.drugowick.timeseriespoc.controller;

import dev.drugowick.timeseriespoc.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminPageController extends BaseController {

    private final AdminService adminService;

    public AdminPageController(AdminService adminService) {
        this.adminService = adminService;
    }

    @RequestMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("userMeasurements", adminService.getCounts());
        return "admin";
    }
}
