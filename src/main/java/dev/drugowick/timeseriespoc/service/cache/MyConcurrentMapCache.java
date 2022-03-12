package dev.drugowick.timeseriespoc.service.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.concurrent.ConcurrentMapCache;

public class MyConcurrentMapCache extends ConcurrentMapCache {

    private static final Logger log = LoggerFactory.getLogger(MyConcurrentMapCache.class);

    public MyConcurrentMapCache(String name) {
        super(name);
    }

    public void clearEntriesWithPrefix(String prefix) {
        this.getNativeCache().forEach((key, value) -> {
            if (key.toString().startsWith(prefix)) {
                log.info("Removing cache entry {}", key);
                this.getNativeCache().remove(key);
            }
        });
    }
}
