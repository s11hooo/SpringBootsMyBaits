package kopo.poly2.service;

import kopo.poly2.config.CacheConfig;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

class CacheConfigTest {
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test void bothCachesHaveTenMinuteExpiry() {
        try (var context = new AnnotationConfigApplicationContext(CacheConfig.class)) {
            var manager = context.getBean(javax.cache.CacheManager.class);
            for (String name : new String[]{"notice", "weather"}) {
                org.ehcache.Cache cache = manager.getCache(name).unwrap(org.ehcache.Cache.class);
                assertEquals(Duration.ofMinutes(10), cache.getRuntimeConfiguration()
                        .getExpiryPolicy().getExpiryForCreation("test", "value"));
            }
        }
    }
}