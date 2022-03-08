package dev.drugowick.timeseriespoc.controller;

import dev.drugowick.timeseriespoc.controller.dto.SearchParams;
import dev.drugowick.timeseriespoc.domain.entity.Measurement;
import dev.drugowick.timeseriespoc.domain.repository.EventsRepository;
import dev.drugowick.timeseriespoc.domain.repository.MeasurementsRepository;
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

    // TODO Service Layer
    private final MeasurementsRepository measurementsRepository;
    private final EventsRepository eventsRepository;

    public HomePageController(MeasurementsRepository measurementsRepository, EventsRepository eventsRepository) {
        this.measurementsRepository = measurementsRepository;
        this.eventsRepository = eventsRepository;
    }

    @RequestMapping
    public String homePage(Principal principal, Model model) {

        var search = new SearchParams();

        addMeasurementsToModel(model, search, principal.getName());
        addEventsToModel(model, search, principal.getName());

        model.addAttribute("search", search);

        return "index";
    }

    @RequestMapping("/filter")
    public String homePageWithFilters(@ModelAttribute SearchParams search, Principal principal, Model model) {

        addMeasurementsToModel(model, search, principal.getName());
        addEventsToModel(model, search, principal.getName());

        model.addAttribute("search", search);

        return "index";
    }

    private void addMeasurementsToModel(Model model, SearchParams search, String username) {

        var measurements =
                measurementsRepository.findAllByUsernameAndCreatedDateAfter(
                        username, daysFromNow(search.getDaysOffset()));

        Map<Long, Integer[]> measurementsMap = new HashMap<>();
        measurements.forEach(measurement -> measurementsMap.put(
                measurement.getCreatedDate(),
                new Integer[]{measurement.getHigh(), measurement.getLow(), measurement.getHeartRate(), measurement.getId().intValue()}));
        model.addAttribute("measurements", measurementsMap);

        measurements.stream().map(Measurement::getHigh).reduce((i, j) -> i > j ? i : j)
                .ifPresent(max -> model.addAttribute("maxMeasurement", max));
    }

    private void addEventsToModel(Model model, SearchParams search, String username) {
        var events =
                eventsRepository.findAllByUsernameAndCreatedDateAfter(
                        username, daysFromNow(search.getDaysOffset()));

        Map<Long, String> eventsMap = new HashMap<>();
        events.forEach(event -> eventsMap.put(event.getCreatedDate(), event.getDescription()));
        model.addAttribute("events", eventsMap);
    }

    private long daysFromNow(long daysOffset) {
        return Instant.now().toEpochMilli() - daysOffset * 60 * 60 * 24 * 1000;
    }

}
