package dev.drugowick.timeseriespoc.config;

import dev.drugowick.timeseriespoc.service.UserService;
import dev.drugowick.timeseriespoc.service.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Configuration
@EnableWebSecurity
@ConditionalOnProperty(name = "app.dev-mode", havingValue = "false")
public class MyConfiguration {

    private final UserService userService;

    public MyConfiguration(UserService userService) {
        this.userService = userService;
    }

    @Bean
    CustomSuccessHandler successHandler() {
        return new CustomSuccessHandler(this.userService);
    }

    @Bean
    CustomAuthoritiesMapper authoritiesMapper() {
        return new CustomAuthoritiesMapper();
    }

    @Bean
    SecurityConfig securityConfig() {
        return new SecurityConfig(successHandler(), authoritiesMapper());
    }
}

/**
 * The default security config, OAuth2.
 */
@Configuration
@ConditionalOnProperty(name = "app.dev-mode", havingValue = "false")
class SecurityConfig {

    private final CustomSuccessHandler successHandler;
    private final GrantedAuthoritiesMapper authoritiesMapper;

    SecurityConfig(CustomSuccessHandler successHandler, GrantedAuthoritiesMapper authoritiesMapper) {
        this.successHandler = successHandler;
        this.authoritiesMapper = authoritiesMapper;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .oauth2Login(configurer -> configurer
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userAuthoritiesMapper(this.authoritiesMapper))
                        .successHandler(this.successHandler));
        return http.build();
    }
}

/**
 * Authorities mapper to intercept authorities from provider and assign ADMIN role to my user (hardcoded).
 * (from <a href="https://docs.spring.io/spring-security/reference/servlet/oauth2/login/advanced.html#oauth2login-advanced-map-authorities-grantedauthoritiesmapper">...</a>)
 */
class CustomAuthoritiesMapper implements GrantedAuthoritiesMapper {
    @Override
    public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
        authorities.forEach(authority -> {
            if (authority instanceof OidcUserAuthority oidcUserAuthority) {
                var email = oidcUserAuthority.getIdToken().getEmail();
                if ("bruno.drugowick@gmail.com".equals(email)) {
                    SimpleGrantedAuthority admin = new SimpleGrantedAuthority("ROLE_ADMIN");
                    mappedAuthorities.add(admin);
                }
            }
            mappedAuthorities.add(authority);
        });
        return mappedAuthorities;
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
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {

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
