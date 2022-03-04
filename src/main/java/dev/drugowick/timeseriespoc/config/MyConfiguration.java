package dev.drugowick.timeseriespoc.config;

import dev.drugowick.timeseriespoc.domain.entity.Event;
import dev.drugowick.timeseriespoc.domain.entity.Measurement;
import dev.drugowick.timeseriespoc.domain.repository.EventsRepository;
import dev.drugowick.timeseriespoc.domain.repository.MeasurementsRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
@EnableWebSecurity
public class MyConfiguration {

    private final MeasurementsRepository measurementsRepository;
    private final EventsRepository eventsRepository;

    public MyConfiguration(MeasurementsRepository measurementsRepository, EventsRepository eventsRepository) {
        this.measurementsRepository = measurementsRepository;
        this.eventsRepository = eventsRepository;
    }

    @Bean
    @ConditionalOnProperty(name="app.dev-mode", havingValue = "false")
    SecurityConfig securityConfig() {
        return new SecurityConfig();
    }

    @Bean
    @ConditionalOnProperty(name="app.dev-mode", havingValue = "true")
    DevSecurityConfig devSecurityConfig() {
        return new DevSecurityConfig();
    }

    @Bean
    @ConditionalOnProperty(name="app.dev-mode", havingValue = "true")
    DevData developmentData() {
        return new DevData(this.measurementsRepository, this.eventsRepository);
    }
}

/**
 * The production security config, OAuth2.
 */
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .oauth2Login();
    }
}

/**
 * The local dev security config, that enables a developer/developer user
 */
class DevSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(DevUtil.USERNAME).password("{noop}" + DevUtil.USERNAME).roles("USER");
    }
}

/**
 * A class data inserts data for the developer user
 */
class DevData {

    private final MeasurementsRepository measurementsRepository;
    private final EventsRepository eventsRepository;

    public DevData(MeasurementsRepository repository, EventsRepository eventsRepository) {
        this.measurementsRepository = repository;
        this.eventsRepository = eventsRepository;

        System.out.println("Adding development data.");
        addData();
    }

    private void addData() {
        for (int i = 0; i < 50; i++) {
            Random r = new Random();

            var bp = new Measurement();
            bp.setHigh(ThreadLocalRandom.current().nextInt(120, 199));
            bp.setLow(ThreadLocalRandom.current().nextInt(60, 110));
            bp.setHeartRate(ThreadLocalRandom.current().nextInt(40, 110));
            bp.setUsername(DevUtil.USERNAME);
            measurementsRepository.save(bp);

            if (i % 5 == 0) {
                var e = new Event();
                e.setDescription("Event number " + i);
                e.setUsername(DevUtil.USERNAME);
                eventsRepository.save(e);
            }
        }
    }
}

record DevUtil() {
    public static String USERNAME = "developer";
}