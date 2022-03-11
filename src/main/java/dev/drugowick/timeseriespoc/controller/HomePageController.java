package dev.drugowick.timeseriespoc.controller;

import dev.drugowick.timeseriespoc.controller.dto.SearchParams;
import dev.drugowick.timeseriespoc.domain.entity.Measurement;
import dev.drugowick.timeseriespoc.service.UserData;
import dev.drugowick.timeseriespoc.service.UserDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HomePageController extends BaseController {

    private final UserDataService userDataService;

    public HomePageController(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    @RequestMapping
    public String homePage(Principal principal, Model model) {

        var search = new SearchParams();

        var userData =
                userDataService.findAllByUsernameAndCreatedDateAfter(
                        principal.getName(), daysFromNow(search.getDaysOffset()));

        addMeasurementsToModel(userData, model);
        addEventsToModel(userData, model);

        model.addAttribute("search", search);

        return "index";
    }

    @RequestMapping("/filter")
    public String homePageWithFilters(@ModelAttribute SearchParams search, Principal principal, Model model) {

        var userData =
                userDataService.findAllByUsernameAndCreatedDateAfter(
                        principal.getName(), daysFromNow(search.getDaysOffset()));

        addMeasurementsToModel(userData, model);
        addEventsToModel(userData, model);

        model.addAttribute("search", search);

        return "index";
    }

    private void addMeasurementsToModel(UserData userData, Model model) {
        Map<Long, Integer[]> measurementsMap = new HashMap<>();
        userData.getMeasurementList().forEach(measurement -> measurementsMap.put(
                measurement.getCreatedDate(),
                new Integer[]{measurement.getHigh(), measurement.getLow(), measurement.getHeartRate(), measurement.getId().intValue()}));
        model.addAttribute("measurements", measurementsMap);

        userData.getMeasurementList().stream().map(Measurement::getHigh).reduce((i, j) -> i > j ? i : j)
                .ifPresent(max -> model.addAttribute("maxMeasurement", max));
    }

    private void addEventsToModel(UserData userData, Model model) {
        Map<Long, String> eventsMap = new HashMap<>();
        userData.getEventList().forEach(event -> eventsMap.put(event.getCreatedDate(), event.getDescription()));
        model.addAttribute("events", eventsMap);
    }

    private long daysFromNow(long daysOffset) {
        return Instant.now().toEpochMilli() - daysOffset * 60 * 60 * 24 * 1000;
    }

}
