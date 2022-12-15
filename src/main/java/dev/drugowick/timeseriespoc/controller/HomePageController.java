package dev.drugowick.timeseriespoc.controller;

import dev.drugowick.timeseriespoc.controller.dto.SearchParams;
import dev.drugowick.timeseriespoc.service.UserDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class HomePageController extends BaseController {

    private final UserDataService userDataService;

    public HomePageController(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    @RequestMapping("/")
    public String homePage(Principal principal, Model model) {

        var search = new SearchParams();

        var startDate = daysFromNow(search.getDaysOffset());
        var userData =
                userDataService.findAllByUsernameAndCreatedDateAfterAndCreatedDateBefore(
                        principal.getName(), startDate, this.now());

        model.addAttribute("userData", userData);
        model.addAttribute("search", search);

        return "index";
    }

    @RequestMapping("/filter")
    public String homePageWithFilters(@ModelAttribute SearchParams search, Principal principal, Model model) {

        var startDate = daysFromNow(search.getDaysOffset());
        var userData =
                userDataService.findAllByUsernameAndCreatedDateAfterAndCreatedDateBefore(
                        principal.getName(), startDate, this.now());

        model.addAttribute("userData", userData);
        model.addAttribute("search", search);

        return "index";
    }

    private long daysFromNow(long daysOffset) {
        return this.now() - daysOffset * 60 * 60 * 24 * 1000;
    }

}
