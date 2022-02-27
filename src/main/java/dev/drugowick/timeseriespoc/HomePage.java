package dev.drugowick.timeseriespoc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Arrays;

@Controller
public class HomePage extends BaseController {

    @GetMapping
    public String homePage(Principal principal, Model model) {
        String username = principal.getName();
        model.addAttribute("measurements", Arrays.asList(
                "138,98",
                "142,88",
                "135,90",
                "142,90",
                "132,93",
                "132,85"
        ));
        return "index";
    }
}
