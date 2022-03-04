package dev.drugowick.timeseriespoc.controller;

import dev.drugowick.timeseriespoc.controller.dto.EventInput;
import dev.drugowick.timeseriespoc.domain.entity.Event;
import dev.drugowick.timeseriespoc.domain.repository.EventsRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class NewEventController {

    // TODO Service Layer
    private final EventsRepository repository;

    public NewEventController(EventsRepository repository) {
        this.repository = repository;
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

        var event = eventFromInput(eventInput, principal.getName());
        repository.save(event);
        return "redirect:/";
    }

    private Event eventFromInput(EventInput eventInput, String name) {
        var e = new Event();
        e.setDescription(eventInput.getDescription());
        e.setUsername(name);

        return e;
    }
}
