package dev.drugowick.timeseriespoc.service;

import dev.drugowick.timeseriespoc.domain.entity.Event;
import dev.drugowick.timeseriespoc.domain.entity.Measurement;
import dev.drugowick.timeseriespoc.domain.repository.EventsRepository;
import dev.drugowick.timeseriespoc.domain.repository.MeasurementsRepository;
import dev.drugowick.timeseriespoc.domain.repository.SnapshotRepository;
import dev.drugowick.timeseriespoc.service.cache.MyConcurrentMapCache;
import dev.drugowick.timeseriespoc.service.cache.UserBasedCacheConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserDataService {

    private static final Logger log = LoggerFactory.getLogger(UserDataService.class);

    private final MeasurementsRepository measurementsRepository;
    private final EventsRepository eventsRepository;
    private final CacheManager cacheManager;
    private final SnapshotRepository snapshotRepository;

    public UserDataService(MeasurementsRepository measurementsRepository, EventsRepository eventsRepository, CacheManager cacheManager, SnapshotRepository snapshotRepository) {
        this.measurementsRepository = measurementsRepository;
        this.eventsRepository = eventsRepository;
        this.cacheManager = cacheManager;
        this.snapshotRepository = snapshotRepository;
    }

    @Cacheable(key = "#username.concat(@cacheCalculator.getCacheTimestamp(#daysFromNow))", cacheNames = UserBasedCacheConfig.CACHE_NAME)
    public UserData findAllByUsernameAndCreatedDateAfterAndCreatedDateBefore(String username, long daysFromNow, long now) {
        log.info("Running DB search for {} with an offset of {} from {}", username, daysFromNow, now);
        var measurements =
                measurementsRepository.findAllByUsernameAndCreatedDateAfterAndCreatedDateBefore(username, daysFromNow, now);
        return new UserData(
                daysFromNow,
                now,
                getMaxInSet(measurements),
                measurements,
                eventsRepository.findAllByUsernameAndCreatedDateAfterAndCreatedDateBefore(username, daysFromNow, now));
    }

    private Integer getMaxInSet(List<Measurement> measurements) {
        return measurements.stream().map(Measurement::getHigh).reduce((i, j) -> i > j ? i : j)
                .orElse(200);
    }

    public void saveEvent(String username, Event event) {
        clearUserCache(username);
        event.setUsername(username);
        eventsRepository.save(event);
    }

    public void saveMeasurement(String username, Measurement measurement) {
        clearUserCache(username);
        measurement.setUsername(username);
        measurementsRepository.save(measurement);
    }

    private void clearUserCache(String username) {
        cacheManager.getCacheNames().forEach(cacheName -> {
            log.info("Clearing cache {}", cacheName);
            Objects.requireNonNull((MyConcurrentMapCache) cacheManager.getCache(cacheName)).clearEntriesWithPrefix(username);
        });
    }

    public UserData findBySnapshotId(UUID snapshotId) {
        var snapshot = snapshotRepository.getById(snapshotId);
        var startDate = snapshot.getStartDate();
        var endDate = snapshot.getEndDate();
        var username = snapshot.getUsername();
        var measurements = measurementsRepository.findAllByUsernameAndCreatedDateAfterAndCreatedDateBefore(username, startDate, endDate);
        var events = eventsRepository.findAllByUsernameAndCreatedDateAfterAndCreatedDateBefore(username, startDate, endDate);
        return new UserData(
                startDate,
                endDate,
                getMaxInSet(measurements),
                measurements,
                events
        );
    }
}

