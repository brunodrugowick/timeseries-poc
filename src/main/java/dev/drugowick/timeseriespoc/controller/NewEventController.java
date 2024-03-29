package dev.drugowick.timeseriespoc.controller;

import dev.drugowick.timeseriespoc.controller.dto.EventInput;
import dev.drugowick.timeseriespoc.domain.entity.Event;
import dev.drugowick.timeseriespoc.service.UserDataService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
public class NewEventController extends BaseController {

    private final UserDataService userDataService;

    public NewEventController(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    @RequestMapping(value = "/new-event", method = RequestMethod.GET)
    public String newEvent(Model model) {
        var eventInput = new EventInput();
        model.addAttribute("event", eventInput);
        return "new-event";
    }

    @RequestMapping(value = "/new-event", method = RequestMethod.POST)
    public String save(Principal principal,
                       @ModelAttribute("event") @Valid EventInput eventInput, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "new-event";

        var event = eventFromInput(eventInput);
        userDataService.saveEvent(principal.getName(), event);
        return "redirect:/";
    }

    private Event eventFromInput(EventInput eventInput) {
        var e = new Event();
        e.setDescription(eventInput.getDescription());

        return e;
    }
}
