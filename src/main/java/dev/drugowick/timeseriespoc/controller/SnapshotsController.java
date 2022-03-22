package dev.drugowick.timeseriespoc.controller;

import dev.drugowick.timeseriespoc.controller.dto.SearchParams;
import dev.drugowick.timeseriespoc.service.SnapshotService;
import dev.drugowick.timeseriespoc.service.UserData;
import dev.drugowick.timeseriespoc.service.UserDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.UUID;

@Controller
public class SnapshotsController {

    private final SnapshotService snapshotService;
    private final UserDataService userDataService;

    public SnapshotsController(SnapshotService snapshotService, UserDataService userDataService) {
        this.snapshotService = snapshotService;
        this.userDataService = userDataService;
    }

    @RequestMapping(value = "/snapshots", method = RequestMethod.GET)
    public String snapshotsPage(Principal principal, Model model) {
        model.addAttribute("snapshots", snapshotService.findAllByUsername(principal.getName()));
        return "snapshots";
    }

    @RequestMapping(value = "/snapshots/{uuid}", method = RequestMethod.GET)
    public String snapshotPage(@PathVariable("uuid") UUID uuid, Model model) {
        var userData = userDataService.findBySnapshotId(uuid);
        var snapshot = snapshotService.getByUuid(uuid);
        model.addAttribute("userData", userData);
        model.addAttribute("snapshot", snapshot);

        return "snapshot";
    }

    // TODO Save a snapshot should be another screen.
    // TODO Need to transform the views in fragments to reuse some code
    // TODO Review all the SnapshotsService now that I have everything (almost) working fine.

    @RequestMapping(value = "/snapshots", method = RequestMethod.POST)
    public String saveSnapshot(@ModelAttribute("search") SearchParams search,
                               @ModelAttribute("userData") UserData userData,
                               Principal principal,
                               Model model) {
        snapshotService.save(userData.getStartDate(), userData.getEndDate(), principal.getName());

        model.addAttribute("search", search);
        return "redirect:/";
    }
}
