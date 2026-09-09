package org.zirota.islab1.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zirota.islab1.entity.Coordinates;
import org.zirota.islab1.entity.Location;
import org.zirota.islab1.entity.Person;
import org.zirota.islab1.exceptions.NotFoundException;
import org.zirota.islab1.repository.CoordinatesRepository;
import org.zirota.islab1.repository.LocationRepository;
import org.zirota.islab1.repository.PersonRepository;

import java.util.List;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final LocationRepository locationRepository;
    private final CoordinatesRepository coordinatesRepository;

    public PersonService(PersonRepository personRepository,
                         LocationRepository locationRepository,
                         CoordinatesRepository coordinatesRepository) {
        this.personRepository = personRepository;
        this.locationRepository = locationRepository;
        this.coordinatesRepository = coordinatesRepository;
    }

    public Page<Person> getAll(int page, int size, String sortBy, String direction, String name) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        if (name != null && !name.isBlank()) {
            return personRepository.findByName(name, pageable);
        }
        return personRepository.findAll(pageable);
    }
    public Person getById(int id) {
        return personRepository.findById(id).orElseThrow(() -> new RuntimeException("Человека нет"));
    }

    @Transactional
    public Person create(Person person) {
        Location loc = locationRepository.findById(person.getLocation().getId()).orElseThrow(() ->
                new NotFoundException("Нет такой локации"));
        Coordinates cord = coordinatesRepository.findById(person.getCoordinates().getId()).orElseThrow(() ->
                new NotFoundException("нет таких координат"));

        person.setLocation(loc);
        person.setCoordinates(cord);
        return personRepository.save(person);

    }
    @Transactional
    public Person update(Integer id, Person updatedPerson) {
        Person person = getById(id);

        Coordinates coordinates = coordinatesRepository
                .findById(updatedPerson.getCoordinates().getId())
                .orElseThrow(() ->
                        new NotFoundException("Coordinates not found"));

        Location location = locationRepository
                .findById(updatedPerson.getLocation().getId())
                .orElseThrow(() ->
                        new NotFoundException("Location not found"));

        person.setCoordinates(coordinates);
        person.setLocation(location);
        person.setName(updatedPerson.getName());
        person.setEyeColor(updatedPerson.getEyeColor());
        person.setHairColor(updatedPerson.getHairColor());
        person.setHeight(updatedPerson.getHeight());
        person.setNationality(updatedPerson.getNationality());

        return personRepository.save(person);

    }

    @Transactional
    public void delete(Integer id) {
        Person person = getById(id);
        personRepository.delete(person);
    }
}
