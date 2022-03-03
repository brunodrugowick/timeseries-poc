package dev.drugowick.timeseriespoc.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

public class BaseController {

    @ModelAttribute("useremail")
    public String fullname(Principal principal) {
        if (principal == null) return "";
        if (principal instanceof OAuth2AuthenticationToken) {
            return ((OAuth2AuthenticationToken) principal).getPrincipal().getAttribute("email");
        }
        return principal.getName();
    }
}
