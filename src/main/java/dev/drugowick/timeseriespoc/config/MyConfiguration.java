package dev.drugowick.timeseriespoc.config;

import dev.drugowick.timeseriespoc.domain.entity.Event;
import dev.drugowick.timeseriespoc.domain.entity.Measurement;
import dev.drugowick.timeseriespoc.domain.repository.EventsRepository;
import dev.drugowick.timeseriespoc.domain.repository.MeasurementsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.time.Instant;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
@EnableWebSecurity
@EnableScheduling
public class MyConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MyConfiguration.class);

    private final MeasurementsRepository measurementsRepository;
    private final EventsRepository eventsRepository;
    private final CacheManager cacheManager;

    public MyConfiguration(MeasurementsRepository measurementsRepository, EventsRepository eventsRepository, CacheManager cacheManager) {
        this.measurementsRepository = measurementsRepository;
        this.eventsRepository = eventsRepository;
        this.cacheManager = cacheManager;
    }

    @Scheduled(fixedDelay = 300000)
    public void clearAllCaches() {
        log.info("Cleaning all caches from {}", cacheManager);
        cacheManager.getCacheNames()
                .forEach(cacheName -> Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());
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
            var createdDateOffset = (long) i * 60 * 60 * 24 * 1000;
            Random r = new Random();

            var bp = new Measurement();
            bp.setHigh(ThreadLocalRandom.current().nextInt(110, 199));
            bp.setLow(ThreadLocalRandom.current().nextInt(60, 110));
            bp.setHeartRate(ThreadLocalRandom.current().nextInt(40, 110));
            bp.setUsername(DevUtil.USERNAME);
            measurementsRepository.save(bp);

            // Changing create date for dev data after saving
            bp.setCreatedDate(Instant.now().toEpochMilli() - createdDateOffset);
            measurementsRepository.save(bp);

            if (i % 5 == 0) {
                var e = new Event();
                e.setDescription("Event number " + i);
                e.setUsername(DevUtil.USERNAME);
                eventsRepository.save(e);

                // Changing create date for dev data after saving
                e.setCreatedDate(Instant.now().toEpochMilli() - createdDateOffset);
                eventsRepository.save(e);
            }
        }
    }
}

record DevUtil() {
    public static String USERNAME = "developer";
}