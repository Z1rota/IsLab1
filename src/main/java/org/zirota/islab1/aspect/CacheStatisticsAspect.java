package org.zirota.islab1.aspect;

import jakarta.persistence.EntityManagerFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.zirota.islab1.config.CacheStatisticsSwitch;

@Aspect
@Component
public class CacheStatisticsAspect {
    private static final Logger log = LoggerFactory.getLogger(CacheStatisticsAspect.class);
    private final Statistics statistics;
    private final CacheStatisticsSwitch cacheSwitch;

    public CacheStatisticsAspect(EntityManagerFactory emf, CacheStatisticsSwitch cacheSwitch) {
        this.statistics = emf.unwrap(SessionFactory.class).getStatistics();
        this.cacheSwitch = cacheSwitch;
    }

    @Around(
            "execution(* org.zirota.islab1.service.PersonService.getAll(..)) || " +
                    "execution(* org.zirota.islab1.service.PersonService.getById(..)) || " +
                    "execution(* org.zirota.islab1.service.CoordinatesService.findAll(..)) || " +
                    "execution(* org.zirota.islab1.service.CoordinatesService.findById(..)) || " +
                    "execution(* org.zirota.islab1.service.LocationService.findAll(..)) || " +
                    "execution(* org.zirota.islab1.service.LocationService.findById(..))"
    )
    public Object logCacheStatistics(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } finally {
            if (cacheSwitch.isEnabled()) {
                log.info("Method: {} | L2 Hits: {} | L2 Misses: {} | " +
                        "Query Hits: {} | Query Misses: {}", joinPoint.getSignature().toShortString(),
                        statistics.getSecondLevelCacheHitCount(),statistics.getSecondLevelCacheMissCount(),
                        statistics.getQueryCacheHitCount(),statistics.getQueryCacheMissCount());
            }
        }
    }
}
