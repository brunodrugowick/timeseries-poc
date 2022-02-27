package dev.drugowick.timeseriespoc;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.ModelAttribute;

public class BaseController {

    @ModelAttribute("useremail")
    public String fullname(OAuth2AuthenticationToken authentication) {
        return authentication.getPrincipal().getAttribute("name") + " (" +
                authentication.getPrincipal().getAttribute("email") + ")";
    }
}
