package org.zirota.islab1.controller;

import org.springframework.web.bind.annotation.*;
import org.zirota.islab1.config.CacheStatisticsSwitch;

import java.util.Map;

@RequestMapping("/api/admin/cache-statistics")
@RestController
public class AdminCacheController {

    private final CacheStatisticsSwitch cacheSwitch;
    public AdminCacheController(final CacheStatisticsSwitch cacheSwitch) {
        this.cacheSwitch = cacheSwitch;
    }

    @PostMapping
    public Map<String,Boolean> setEnabled(@RequestParam boolean enabled) {
        cacheSwitch.setEnabled(enabled);
        return Map.of("enabled", cacheSwitch.isEnabled());
    }
    @GetMapping
    public Map<String,Boolean> getEnabled() {
        return Map.of("enabled", cacheSwitch.isEnabled());
    }
}
