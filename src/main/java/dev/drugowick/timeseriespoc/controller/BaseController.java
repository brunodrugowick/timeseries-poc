package dev.drugowick.timeseriespoc.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.time.Instant;

public class BaseController {

    @ModelAttribute("useremail")
    public String fullname(Principal principal) {
        if (principal == null) return "";
        if (principal instanceof OAuth2AuthenticationToken) {
            return ((OAuth2AuthenticationToken) principal).getPrincipal().getAttribute("email");
        }
        return principal.getName();
    }

    // This is a gambiarra, the notion of "now" should be related to the data or the same "now" from when doing the
    // actual request for the data... anywayyyyyy...
    @ModelAttribute("now")
    public Long now() {
        return Instant.now().toEpochMilli();
    }
}
