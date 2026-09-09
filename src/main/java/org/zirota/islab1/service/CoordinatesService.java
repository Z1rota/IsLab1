package org.zirota.islab1.service;

import org.springframework.stereotype.Service;
import org.zirota.islab1.entity.Coordinates;
import org.zirota.islab1.repository.CoordinatesRepository;

import java.util.List;

@Service
public class CoordinatesService {
    private final CoordinatesRepository coordinatesRepository;
    public CoordinatesService(CoordinatesRepository coordinatesRepository) {
        this.coordinatesRepository = coordinatesRepository;
    }

    public List<Coordinates> findAll() {
        return coordinatesRepository.findAll();
    }
    public Coordinates findById(Long id) {
        return coordinatesRepository.findById(id).orElseThrow(() -> new RuntimeException("Таких координат нет"));
    }
    public Coordinates create(Coordinates coordinates) {
        return coordinatesRepository.save(coordinates);
    }
}
