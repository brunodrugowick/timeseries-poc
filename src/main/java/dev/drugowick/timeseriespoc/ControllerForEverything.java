package dev.drugowick.timeseriespoc;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@Controller
@Profile("!personal && !development")
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
        var measurement = new BloodPressure();
        model.addAttribute("measurement", measurement);
        return "new-measurement";
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String save(Principal principal,
                       @ModelAttribute("measurement") @Valid BloodPressure measurement, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "new-measurement";

        var username = principal.getName();
        measurement.setUsername(username);
        repository.save(measurement);
        return "redirect:/";
    }
}
