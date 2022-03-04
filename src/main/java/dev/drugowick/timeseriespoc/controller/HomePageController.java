package dev.drugowick.timeseriespoc.controller;

import dev.drugowick.timeseriespoc.domain.repository.EventsRepository;
import dev.drugowick.timeseriespoc.domain.repository.MeasurementsRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
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

    @GetMapping
    public String homePage(Principal principal, Model model) {

        var username = principal.getName();
        var measurements = measurementsRepository.findAllByUsername(username);
        var events = eventsRepository.findAllByUsername(username);

        Map<Long, Integer[]> measurementsMap = new HashMap<>();
        measurements.forEach(measurement -> measurementsMap.put(
                measurement.getCreatedDate(),
                new Integer[]{measurement.getHigh(), measurement.getLow(), measurement.getHeartRate(), measurement.getId().intValue()}));

        Map<Long, String> eventsMap = new HashMap<>();
        events.forEach(event -> eventsMap.put(event.getCreatedDate(), event.getDescription()));

        model.addAttribute("measurements", measurementsMap);
        model.addAttribute("events", eventsMap);

        return "index";
    }

}
