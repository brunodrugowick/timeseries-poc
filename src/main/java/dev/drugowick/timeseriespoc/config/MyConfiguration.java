package dev.drugowick.timeseriespoc.config;

import dev.drugowick.timeseriespoc.service.UserService;
import dev.drugowick.timeseriespoc.service.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Objects;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Configuration
@EnableWebSecurity
@EnableScheduling
@ConditionalOnProperty(name = "app.dev-mode", havingValue = "false")
public class MyConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MyConfiguration.class);

    private final UserService userService;
    private final CacheManager cacheManager;

    public MyConfiguration(UserService userService, CacheManager cacheManager) {
        this.userService = userService;
        this.cacheManager = cacheManager;
    }

    @Scheduled(fixedDelay = 300000)
    public void clearAllCaches() {
        log.info("Cleaning all caches from {}", cacheManager);
        cacheManager.getCacheNames()
                .forEach(cacheName -> Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());
    }

    @Bean
    CustomSuccessHandler successHandler() {
        return new CustomSuccessHandler(this.userService);
    }

    @Bean
    SecurityConfig securityConfig() {
        return new SecurityConfig(successHandler());
    }
}

/**
 * The default security config, OAuth2.
 */
class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomSuccessHandler successHandler;

    SecurityConfig(CustomSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .successHandler(this.successHandler);
    }
}

/**
 * Success handler to register or update the user when they log in
 */
class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomSuccessHandler.class);

    private static final String GOOGLE_CLIENTID = "google";
    private final UserService userService;

    public CustomSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        String clientId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        OAuth2User oAuthUser = (OAuth2User) authentication.getPrincipal();

        log.info("Successfully authenticated via OAuth with provider " + clientId);

        String userEmail = getUserEmail(clientId, oAuthUser);
        Optional<UserDTO> optionalUserDTO = userService.findOne(userEmail);
        if (optionalUserDTO.isEmpty()) {
            log.info("Creating new user " + userEmail);
            userService.save(newUserDTO(
                    clientId,
                    oAuthUser));
        } else {

            // Provider information gets overwritten based on user email.
            // TODO provide a better implementation, linking the User account with several providers.
            log.info("Updating user provider information " + userEmail);
            UserDTO userDTOToUpdate = optionalUserDTO.get();
            userDTOToUpdate.setProvider(clientId);
            userDTOToUpdate.setProviderId(oAuthUser.getName());

            userService.save(userDTOToUpdate);
        }

        httpServletResponse
                .sendRedirect("/");

    }

    private String getUserEmail(String clientId, OAuth2User user) {
        String userEmail = null;
        if (clientId.equals(GOOGLE_CLIENTID)) { //  || clientId.equals(GITHUB_CLIENTID)
            userEmail = user.getAttributes().get("email").toString();
        }

        return userEmail;
    }

    private UserDTO newUserDTO(String clientId, OAuth2User user) {
        UserDTO userDTO = null;
        if (clientId.equals(GOOGLE_CLIENTID)) { //  || clientId.equals(GITHUB_CLIENTID)
            userDTO = UserDTO.builder()
                    .email(user.getAttributes().get("email").toString())
                    .enabled(true)
                    .fullName(user.getAttributes().get("name").toString())
                    .provider(clientId)
                    .providerId(user.getName())
                    .build();
        }

        return userDTO;
    }
}