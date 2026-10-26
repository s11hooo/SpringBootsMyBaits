package kopo.poly2.config;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kopo.poly2.utill.WeatherCoordinates;
import org.springframework.cache.interceptor.KeyGenerator;
import javax.cache.Caching;
import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean(destroyMethod = "close")
    public javax.cache.CacheManager jCacheManager() {
        javax.cache.CacheManager manager = Caching.getCachingProvider(
                "org.ehcache.jsr107.EhcacheCachingProvider").getCacheManager();
        if (manager.getCache("notice") == null) {
            manager.createCache("notice", Eh107Configuration.fromEhcacheCacheConfiguration(
                    CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                    Object.class, Object.class, ResourcePoolsBuilder.heap(100))
                            .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(10)))
                            .build()));
        }
        if (manager.getCache("weather") == null) {
            manager.createCache("weather", Eh107Configuration.fromEhcacheCacheConfiguration(
                    CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                    Object.class, Object.class, ResourcePoolsBuilder.heap(1000))
                            .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(10)))
                            .build()));
        }
        return manager;
    }

    @Bean
    public JCacheCacheManager cacheManager(javax.cache.CacheManager jCacheManager) {
        JCacheCacheManager manager = new JCacheCacheManager(jCacheManager);
        // Apply cache changes only after a successful database transaction commits.
        manager.setTransactionAware(true);
        return manager;
    }
    @Bean("latLonKeyGen")
    public KeyGenerator latLonKeyGen() {
        return (target, method, params) -> {
            double lat = ((Number) params[0]).doubleValue();
            double lon = ((Number) params[1]).doubleValue();
            WeatherCoordinates.validate(lat, lon);
            return "v1:" + WeatherCoordinates.round(lat) + "_" + WeatherCoordinates.round(lon) + "_metric";
        };
    }
}
