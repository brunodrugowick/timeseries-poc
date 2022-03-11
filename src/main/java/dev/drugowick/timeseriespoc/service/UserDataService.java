package dev.drugowick.timeseriespoc.service;

import dev.drugowick.timeseriespoc.domain.repository.EventsRepository;
import dev.drugowick.timeseriespoc.domain.repository.MeasurementsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserDataService {

    private static final Logger log = LoggerFactory.getLogger(UserDataService.class);

    private final MeasurementsRepository measurementsRepository;
    private final EventsRepository eventsRepository;

    public UserDataService(MeasurementsRepository measurementsRepository, EventsRepository eventsRepository) {
        this.measurementsRepository = measurementsRepository;
        this.eventsRepository = eventsRepository;
    }

    @Cacheable(key = "#username.concat(@cacheCalculator.getCacheTimestamp(#daysFromNow))", cacheNames = "userData")
    public UserData findAllByUsernameAndCreatedDateAfter(String username, long daysFromNow) {
        log.info("Running DB search for {} with a offset of {}", username, daysFromNow);
        return new UserData(
                measurementsRepository.findAllByUsernameAndCreatedDateAfter(username, daysFromNow),
                eventsRepository.findAllByUsernameAndCreatedDateAfter(username, daysFromNow));
    }
}

