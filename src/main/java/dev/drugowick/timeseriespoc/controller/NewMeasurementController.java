package dev.drugowick.timeseriespoc.controller;

import dev.drugowick.timeseriespoc.controller.dto.MeasurementInput;
import dev.drugowick.timeseriespoc.domain.entity.Measurement;
import dev.drugowick.timeseriespoc.domain.repository.MeasurementsRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class NewMeasurementController extends BaseController {

    // TODO Service Layer
    private final MeasurementsRepository repository;

    public NewMeasurementController(MeasurementsRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newMeasurement(Model model) {
        var measurementInput = new MeasurementInput();
        model.addAttribute("measurement", measurementInput);
        return "new-measurement";
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String save(Principal principal,
                       @ModelAttribute("measurement") @Valid MeasurementInput measurementInput, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "new-measurement";

        var measurement = measurementFromInput(measurementInput, principal.getName());
        repository.save(measurement);
        return "redirect:/";
    }

    private Measurement measurementFromInput(MeasurementInput measurementInput, String name) {
        var bp = new Measurement();
        bp.setHigh(measurementInput.getHigh());
        bp.setLow(measurementInput.getLow());
        bp.setHeartRate(measurementInput.getHeartRate());
        bp.setUsername(name);

        return bp;
    }
}
