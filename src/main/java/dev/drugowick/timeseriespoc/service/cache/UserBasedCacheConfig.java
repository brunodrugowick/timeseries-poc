package dev.drugowick.timeseriespoc.service.cache;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class UserBasedCacheConfig extends CachingConfigurerSupport {

    public static final String CACHE_NAME = "main_cache";

    /**
     * Defines a ConcurrentMapCache bean of type MyConcurrentMapCache which is an extension that adds
     * a method to clean on certain keys of the cache.
     *
     * Another, for more complex projects, is to implement a CacheResolver that will determine the usage
     * of a different class for each [whatever-you-want-it-to-be] (username, for example).
     *
     * @return a subclass of ConcurrentMapCache with clearEntriesWithPrefix method.
     */
    @Bean
    ConcurrentMapCache concurrentMapCache() {
        return new MyConcurrentMapCache(CACHE_NAME);
    }

}
