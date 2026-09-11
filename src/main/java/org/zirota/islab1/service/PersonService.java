package org.zirota.islab1.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zirota.islab1.dto.NationalityAdapter;
import org.zirota.islab1.dto.NationalityCountDto;
import org.zirota.islab1.dto.PersonEvent;
import org.zirota.islab1.entity.*;
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
    private final ApplicationEventPublisher eventPublisher;


    public PersonService(PersonRepository personRepository,
                         LocationRepository locationRepository,
                         CoordinatesRepository coordinatesRepository, ApplicationEventPublisher eventPublisher) {
        this.personRepository = personRepository;
        this.locationRepository = locationRepository;
        this.coordinatesRepository = coordinatesRepository;
        this.eventPublisher = eventPublisher;
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
        Coordinates coordinates = coordinatesRepository
                .findById(person.getCoordinates().getId())
                .orElseThrow(() ->
                        new NotFoundException("Coordinates not found"));

        Location location = locationRepository
                .findById(person.getLocation().getId())
                .orElseThrow(() ->
                        new NotFoundException("Location not found"));

        person.setCoordinates(coordinates);
        person.setLocation(location);

        Person savedPerson = personRepository.save(person);

        eventPublisher.publishEvent(
                new PersonEvent("CREATED", savedPerson.getId())
        );
        return savedPerson;
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

        personRepository.save(person);
        eventPublisher.publishEvent(
                new PersonEvent("UPDATED", person.getId()));

        return person;


    }

    @Transactional
    public Integer deleteByNationality(Country nationality) {
        Integer deleted = personRepository.deleteOneByNationality(nationality.name());
        if (deleted == null) {
            throw new NotFoundException(
                    "Пользователей с такой национальностью не найдено"
            );
        }
        eventPublisher.publishEvent(
                new PersonEvent("DELETED", deleted)
        );
        return deleted;
    }

    public Person getMinHeightPerson() {
        Person person = personRepository.findMinHeightPerson();
        if (person == null) {
            throw new NotFoundException("Такого человека нет");
        }
        return person;
    }

    public List<NationalityCountDto> groupByNationality() {
        List<NationalityAdapter> result = personRepository.groupByNationality();
        return result.stream().map(row -> new NationalityCountDto(
                row.getNationality() == null
                        ? null
                :Country.valueOf(row.getNationality()),row.getPersonCount())).toList();
    }

    public Double getHairColor(Color color) {
        return personRepository.getHairColorPercentage(color.name());
    }

    public Long countByEyeColor(Color color) {
        return personRepository.countByEyeColor(color.name());
    }


    @Transactional
    public void delete(Integer id) {
        Person person = getById(id);

        personRepository.delete(person);

        eventPublisher.publishEvent(
                new PersonEvent("DELETED", id)
        );
    }
}
