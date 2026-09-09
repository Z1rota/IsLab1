package org.zirota.islab1.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zirota.islab1.entity.Location;
import org.zirota.islab1.exceptions.NotFoundException;
import org.zirota.islab1.repository.LocationRepository;

import java.util.List;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }
    public List<Location> findAll() {
        return locationRepository.findAll();
    }
    public Location findById(Long id) {
        Location loc = locationRepository.findById(id).orElseThrow(() -> new NotFoundException("Локация не найдена"));
        return loc;
    }

    @Transactional
    public void create(Location location) {
        locationRepository.save(location);
    }
}
