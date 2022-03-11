package dev.drugowick.timeseriespoc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("cacheCalculator")
public class CacheTimestampCalculator {

    private static final Logger log = LoggerFactory.getLogger(CacheTimestampCalculator.class);

    /**
     * Determines the cache timestamp to which the input timestamp belongs.
     *
     * @param timestamp the original timestamp received from the client
     * @return the timestamp which is the cache key for the received timestamp
     */
    public long getCacheTimestamp(long timestamp) {
        var cacheTimestamp = Math.floorDiv(timestamp, 100000)*100000;
        log.info("Extracted cache key {} from received timestamp {}", cacheTimestamp, timestamp);
        return cacheTimestamp;
    }
}
