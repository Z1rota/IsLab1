package org.zirota.islab1.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.zirota.islab1.entity.Coordinates;
import org.zirota.islab1.service.CoordinatesService;

import java.util.List;

@RestController
@RequestMapping("/api/coordinates")
public class CoordinatesController {

    private final CoordinatesService coordinatesService;
    public CoordinatesController(CoordinatesService coordinatesService) {
        this.coordinatesService = coordinatesService;
    }

    @GetMapping()
    public List<Coordinates> getAllCoordinates() {
        return coordinatesService.findAll();
    }
    @GetMapping("/{id}")
    public Coordinates getCoordinates(@PathVariable Long id) {
        return coordinatesService.findById(id);
    }

    @PostMapping()
    public Coordinates createCoordinates(@Valid @RequestBody Coordinates coordinates) {
        return coordinatesService.create(coordinates);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        coordinatesService.delete(id);
    }

}
