package org.zirota.islab1.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.zirota.islab1.entity.Location;
import org.zirota.islab1.service.LocationService;

import java.util.List;

@RestController
@RequestMapping("/api/location")
public class LocationController {
    private final LocationService locationService;
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public List<Location> getAllLocations() {
        return locationService.findAll();
    }

    @GetMapping("/{id}")
    public Location getLocationById(@PathVariable Long id) {
        return locationService.findById(id);
    }
    @PostMapping
    public void createLocation(@Valid @RequestBody Location location) {
        locationService.create(location);
    }



}
