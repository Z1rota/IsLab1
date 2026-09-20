package org.zirota.islab1.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zirota.islab1.dto.CoordinatesEvent;
import org.zirota.islab1.entity.Coordinates;
import org.zirota.islab1.exceptions.NotFoundException;
import org.zirota.islab1.exceptions.ObjectUsedException;
import org.zirota.islab1.repository.CoordinatesRepository;
import org.zirota.islab1.repository.PersonRepository;

import java.util.List;

@Service
public class CoordinatesService {
    private final CoordinatesRepository coordinatesRepository;
    private final PersonRepository personRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CoordinatesService(CoordinatesRepository coordinatesRepository, PersonRepository personRepository, ApplicationEventPublisher eventPublisher) {
        this.coordinatesRepository = coordinatesRepository;
        this.personRepository = personRepository;
        this.eventPublisher = eventPublisher;

    }

    public List<Coordinates> findAll() {
        return coordinatesRepository.findAll();
    }
    public Coordinates findById(Long id) {
        return coordinatesRepository.findById(id).orElseThrow(() -> new NotFoundException("Таких координат нет"));
    }

    @Transactional
    public Coordinates create(Coordinates coordinates) {
        Coordinates cord = coordinatesRepository.save(coordinates);
        eventPublisher.publishEvent(new CoordinatesEvent("CREATED", cord.getId()));
        return cord;
    }

    @Transactional
    public void delete(Long id) {
        Coordinates coordinates = findById(id);
        if (personRepository.existsByCoordinatesId(coordinates.getId())) {
            throw new ObjectUsedException("Координаты связаны с какой-то персоной");
        }
        eventPublisher.publishEvent(new CoordinatesEvent("DELETED", id));
        coordinatesRepository.delete(coordinates);
    }
}
