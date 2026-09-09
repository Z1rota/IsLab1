package org.zirota.islab1.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zirota.islab1.entity.Location;
import org.zirota.islab1.exceptions.NotFoundException;
import org.zirota.islab1.exceptions.ObjectUsedException;
import org.zirota.islab1.repository.LocationRepository;
import org.zirota.islab1.repository.PersonRepository;

import java.util.List;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final PersonRepository personRepository;

    public LocationService(LocationRepository locationRepository, PersonRepository personRepository) {
        this.locationRepository = locationRepository;
        this.personRepository = personRepository;
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


    @Transactional
    public void delete(Long id) {
        Location loc = findById(id);
        if (personRepository.existsByLocationId(loc.getId())) {
            throw new ObjectUsedException("Локация связана с какой-то персоной");
        }
        locationRepository.delete(loc);
    }
}
