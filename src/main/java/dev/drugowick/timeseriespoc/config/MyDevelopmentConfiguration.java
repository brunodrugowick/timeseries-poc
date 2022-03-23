package dev.drugowick.timeseriespoc.config;

import dev.drugowick.timeseriespoc.domain.entity.Event;
import dev.drugowick.timeseriespoc.domain.entity.Measurement;
import dev.drugowick.timeseriespoc.domain.entity.Snapshot;
import dev.drugowick.timeseriespoc.domain.repository.EventsRepository;
import dev.drugowick.timeseriespoc.domain.repository.MeasurementsRepository;
import dev.drugowick.timeseriespoc.domain.repository.SnapshotRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
@EnableWebSecurity
@ConditionalOnProperty(name = "app.dev-mode", havingValue = "true")
public class MyDevelopmentConfiguration {

    private final MeasurementsRepository measurementsRepository;
    private final EventsRepository eventsRepository;
    private final SnapshotRepository snapshotRepository;

    public MyDevelopmentConfiguration(MeasurementsRepository measurementsRepository, EventsRepository eventsRepository, SnapshotRepository snapshotRepository) {
        this.measurementsRepository = measurementsRepository;
        this.eventsRepository = eventsRepository;
        this.snapshotRepository = snapshotRepository;
    }

    @Bean
    DevSecurityConfig devSecurityConfig() {
        return new DevSecurityConfig();
    }

    @Bean
    DevData developmentData() {
        return new DevData(this.measurementsRepository, this.eventsRepository, this.snapshotRepository);
    }
}

/**
 * The local dev security config, that enables a developer/developer user
 */
class DevSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(DevUtil.USERNAME).password("{noop}" + DevUtil.USERNAME).roles("USER")
                .and().withUser("strange").password("{noop}strange").roles("USER");
    }
}

/**
 * A class data inserts data for the developer user
 */
class DevData {

    private final MeasurementsRepository measurementsRepository;
    private final EventsRepository eventsRepository;
    private final SnapshotRepository snapshotRepository;

    public DevData(MeasurementsRepository repository, EventsRepository eventsRepository, SnapshotRepository snapshotRepository) {
        this.measurementsRepository = repository;
        this.eventsRepository = eventsRepository;
        this.snapshotRepository = snapshotRepository;

        System.out.println("Adding development data.");
        addData();
    }

    private void addData() {
        for (int i = 1; i < 51; i++) {
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

            if (i % 10 == 0) {
                var s = new Snapshot();
                s.setStartDate(Instant.now().toEpochMilli() - createdDateOffset);
                s.setEndDate(Instant.now().toEpochMilli() - createdDateOffset + 5 * 60 * 60 * 24 * 1000);
                s.setUsername(DevUtil.USERNAME);
                s.setDescription("This is the snapshot number " + i);
                s.setPublic(i > 20);

                snapshotRepository.save(s);
            }
        }
    }
}

record DevUtil() {
    public static String USERNAME = "developer";
}
