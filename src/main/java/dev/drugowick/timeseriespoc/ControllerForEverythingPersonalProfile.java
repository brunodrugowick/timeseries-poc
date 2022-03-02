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
import java.util.HashMap;
import java.util.Map;

// TODO make this more elegant. Make it in a way I don't need to copy-pasta code, just replace the Principal when it's a dev profile
@Controller
@Profile("personal || development")
public class ControllerForEverythingPersonalProfile extends BaseController {

    private final String DUMMY_USERNAME = "developer";

    private final BloodPressureRepository repository;

    public ControllerForEverythingPersonalProfile(BloodPressureRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String homePage(Principal principal, Model model) {

        var username = DUMMY_USERNAME;
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
        measurement.setUsername(DUMMY_USERNAME);
        repository.save(measurement);
        return "redirect:/";
    }
}
