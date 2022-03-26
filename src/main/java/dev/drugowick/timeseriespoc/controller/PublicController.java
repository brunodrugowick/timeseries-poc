package dev.drugowick.timeseriespoc.controller;

import dev.drugowick.timeseriespoc.service.SnapshotService;
import dev.drugowick.timeseriespoc.service.UserDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@Controller
@RequestMapping("/public")
public class PublicController extends BaseController {

    private final SnapshotService snapshotService;
    private final UserDataService userDataService;

    public PublicController(SnapshotService snapshotService, UserDataService userDataService) {
        this.snapshotService = snapshotService;
        this.userDataService = userDataService;
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public String viewSnapshot(@PathVariable("uuid") UUID uuid, Model model) {
        var optionalSnapshot = snapshotService.getPublicSnapshot(uuid);
        if (optionalSnapshot.isEmpty() || !optionalSnapshot.get().isPublic()) return "redirect:/";

        var snapshot = optionalSnapshot.get();
        var userData = userDataService.findBySnapshotId(uuid);

        model.addAttribute("userData", userData);
        model.addAttribute("snapshot", snapshot);

        return "snapshot";
    }
}
