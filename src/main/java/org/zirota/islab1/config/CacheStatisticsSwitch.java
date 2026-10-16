package org.zirota.islab1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class CacheStatisticsSwitch {

    private final AtomicBoolean enabled;

    public CacheStatisticsSwitch(@Value("${app.cache.statistics.logging-enabled:false}") boolean enabled) {
        this.enabled = new AtomicBoolean(enabled);
    }
    public boolean isEnabled() {
        return enabled.get();
    }
    public void setEnabled(boolean enabled) {
        this.enabled.set(enabled);
    }
}
