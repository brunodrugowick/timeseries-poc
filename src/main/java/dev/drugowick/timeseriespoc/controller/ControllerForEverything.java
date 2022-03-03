package dev.drugowick.timeseriespoc.controller;

import dev.drugowick.timeseriespoc.controller.dto.BloodPressureInput;
import dev.drugowick.timeseriespoc.domain.entity.BloodPressure;
import dev.drugowick.timeseriespoc.domain.repository.BloodPressureRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ControllerForEverything extends BaseController {

    private final BloodPressureRepository repository;

    public ControllerForEverything(BloodPressureRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String homePage(Principal principal, Model model) {

        var username = principal.getName();
        var all = repository.findAllByUsername(username);

        Map<Long, Integer[]> map = new HashMap<>();
        all.forEach(s -> map.put(s.getCreatedDate(), new Integer[]{s.getHigh(), s.getLow(), s.getId().intValue()}));

        model.addAttribute("measurements", map);
        return "index";
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newMeasurement(Model model) {
        var measurement = new BloodPressureInput();
        model.addAttribute("measurement", measurement);
        return "new-measurement";
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String save(Principal principal,
                       @ModelAttribute("measurement") @Valid BloodPressureInput measurement, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "new-measurement";

        var bloodPressure = bloodPressureFromInput(measurement, principal.getName());
        repository.save(bloodPressure);
        return "redirect:/";
    }

    private BloodPressure bloodPressureFromInput(BloodPressureInput measurement, String name) {
        var bp = new BloodPressure();
        bp.setHigh(measurement.getHigh());
        bp.setLow(measurement.getLow());
        bp.setHeartRate(measurement.getHeartRate());
        bp.setUsername(name);

        return bp;
    }
}
