package dev.drugowick.timeseriespoc.controller;

import dev.drugowick.timeseriespoc.service.SnapshotService;
import dev.drugowick.timeseriespoc.service.UserDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @RequestMapping("/snapshots")
    public String snapshotsPage(Principal principal, Model model) {
        model.addAttribute("snapshots", snapshotService.findAllByUsername(principal.getName()));
        return "snapshots";
    }

    @RequestMapping("/snapshots/{uuid}")
    public String snapshotPage(@PathVariable("uuid") UUID uuid, Model model) {
        var userData = userDataService.findBySnapshotId(uuid);
        var snapshot = snapshotService.getByUuid(uuid);
        model.addAttribute("userData", userData);
        model.addAttribute("snapshot", snapshot);

        return "snapshot";
    }
}
