package dev.drugowick.timeseriespoc.controller;

import dev.drugowick.timeseriespoc.controller.dto.SnapshotInput;
import dev.drugowick.timeseriespoc.domain.entity.Snapshot;
import dev.drugowick.timeseriespoc.service.SnapshotService;
import dev.drugowick.timeseriespoc.service.UserDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/snapshots")
public class SnapshotsController {

    private final SnapshotService snapshotService;
    private final UserDataService userDataService;

    public SnapshotsController(SnapshotService snapshotService, UserDataService userDataService) {
        this.snapshotService = snapshotService;
        this.userDataService = userDataService;
    }

    @GetMapping
    public String list(Principal principal, Model model) {

        model.addAttribute("snapshots", snapshotService.findAllByUsername(principal.getName()));
        return "snapshots";
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newSnapshot(@ModelAttribute("snapshot")SnapshotInput snapshotInput, Principal principal, Model model) {

        var userData =
                userDataService.findAllByUsernameAndCreatedDateAfterAndCreatedDateBefore(
                        principal.getName(), snapshotInput.getStartDate(), snapshotInput.getEndDate());
        model.addAttribute("userData", userData);
        model.addAttribute("snapshot", snapshotInput);
        return "edit-snapshot";
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String saveSnapshot(@ModelAttribute("snapshot") @Valid SnapshotInput snapshotInput, BindingResult bindingResult,
                               Principal principal, Model model) {
        if (bindingResult.hasErrors()) {
            return "edit-snapshot";
        }

        snapshotService.save(toSnapshot(snapshotInput, principal.getName()));
        return "redirect:/snapshots";
    }

    @RequestMapping(value = "/edit/{uuid}", method = RequestMethod.GET)
    public String editSnapshot(@PathVariable("uuid") UUID uuid, Principal principal, Model model) {
        var snapshot = snapshotService.getByUuid(uuid);
        if (!Objects.equals(principal.getName(), snapshot.getUsername())) return "redirect:/snapshots";

        model.addAttribute("snapshot", snapshot);
        return "edit-snapshot";
    }

    @RequestMapping(value = "/edit/{uuid}", method = RequestMethod.POST)
    public String putSnapshot(@ModelAttribute("snapshot") @Valid SnapshotInput snapshotInput, BindingResult bindingResult,
                               Principal principal, Model model) {
        if (bindingResult.hasErrors()) {
            return "edit-snapshot";
        }

        snapshotService.save(toSnapshot(snapshotInput, principal.getName()));
        return "redirect:/snapshots";
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public String snapshotPage(@PathVariable("uuid") UUID uuid, Model model) {
        var userData = userDataService.findBySnapshotId(uuid);
        var snapshot = snapshotService.getByUuid(uuid);
        model.addAttribute("userData", userData);
        model.addAttribute("snapshot", snapshot);

        return "snapshot";
    }

    private Snapshot toSnapshot(SnapshotInput input, String username) {
        var snapshot = new Snapshot();
        snapshot.setUuid(input.getUuid());
        snapshot.setDescription(input.getDescription());
        snapshot.setStartDate(input.getStartDate());
        snapshot.setEndDate(input.getEndDate());
        snapshot.setPublic(input.isPublic());
        snapshot.setUsername(username);

        return snapshot;
    }
}
