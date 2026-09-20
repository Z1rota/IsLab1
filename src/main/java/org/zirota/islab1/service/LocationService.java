package org.zirota.islab1.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zirota.islab1.dto.LocationEvent;
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
    private final ApplicationEventPublisher eventPublisher;

    public LocationService(LocationRepository locationRepository, PersonRepository personRepository, ApplicationEventPublisher eventPublisher) {
        this.locationRepository = locationRepository;
        this.personRepository = personRepository;
        this.eventPublisher = eventPublisher;
    }
    public List<Location> findAll() {
        return locationRepository.findAll();
    }
    public Location findById(Long id) {
        Location loc = locationRepository.findById(id).orElseThrow(() -> new NotFoundException("Локация не найдена"));
        return loc;
    }

    @Transactional
    public Location create(Location location) {
        Location loc = locationRepository.save(location);
        eventPublisher.publishEvent(new LocationEvent("CREATED",loc.getId()));
        return loc;
    }


    @Transactional
    public void delete(Long id) {
        Location loc = findById(id);
        if (personRepository.existsByLocationId(loc.getId())) {
            throw new ObjectUsedException("Локация связана с какой-то персоной");
        }
        locationRepository.delete(loc);
        eventPublisher.publishEvent(new LocationEvent("DELETED",id));
    }
}
