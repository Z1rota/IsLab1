package org.zirota.islab1.service;


import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.zirota.islab1.dto.CoordinatesEvent;
import org.zirota.islab1.dto.LocationEvent;
import org.zirota.islab1.dto.PersonEvent;
import org.zirota.islab1.dto.PersonImportRow;
import org.zirota.islab1.entity.Coordinates;
import org.zirota.islab1.entity.Location;
import org.zirota.islab1.entity.Person;
import org.zirota.islab1.exceptions.DuplicatePersonException;
import org.zirota.islab1.repository.CoordinatesRepository;
import org.zirota.islab1.repository.LocationRepository;
import org.zirota.islab1.repository.PersonRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ImportService {

    private final PersonImportParser parser;
    private final PersonRepository personRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final LocationRepository locationRepository;
    private final ApplicationEventPublisher eventPublisher;


    public ImportService(PersonImportParser parser, PersonRepository personRepository,
                         CoordinatesRepository coordinatesRepository,
                         LocationRepository locationRepository, ApplicationEventPublisher eventPublisher) {
        this.parser = parser;
        this.personRepository = personRepository;
        this.coordinatesRepository = coordinatesRepository;
        this.locationRepository = locationRepository;
        this.eventPublisher = eventPublisher;
    }


    @Transactional(isolation = Isolation.SERIALIZABLE)
    public int importPerson(MultipartFile file) {
        List<PersonImportRow> rows = parser.parse(file);

        validateUniquePerson(rows);

        for (PersonImportRow row : rows) {
            createPerson(row);
        }
        return rows.size();
    }

    private void validateUniquePerson(List<PersonImportRow> rows) {
        Set<PersonUniqueKey> keys = new HashSet();

        for (PersonImportRow row : rows) {
            String name = row.name().trim();
            PersonUniqueKey key = new PersonUniqueKey(name,row.height());

            if (!keys.add(key)) {
                throw new DuplicatePersonException("В файле есть объекты с одинаковым name&height");
            }
            if (personRepository.existsByNameAndHeight(name, row.height())) {
                throw new DuplicatePersonException("Person с " + name + "и height " + row.height() + "уже существует");
            }
        }
    }
    private void createPerson(PersonImportRow row) {

        Coordinates coordinates = new Coordinates();
        coordinates.setX(row.coordinatesX());
        coordinates.setY(row.coordinatesY());

        coordinates = coordinatesRepository.save(coordinates);

        Location location = new Location();
        location.setX(row.locationX());
        location.setY(row.locationY());
        location.setZ(row.locationZ());

        location = locationRepository.save(location);

        Person person = new Person();

        person.setName(row.name().trim());
        person.setHeight(row.height());
        person.setCoordinates(coordinates);
        person.setLocation(location);
        person.setNationality(row.nationality());
        person.setEyeColor(row.eyeColor());
        person.setHairColor(row.hairColor());
        Person saved = personRepository.save(person);

        eventPublisher.publishEvent(new CoordinatesEvent("CREATED",coordinates.getId()));
        eventPublisher.publishEvent(new LocationEvent("CREATED",location.getId()));
        eventPublisher.publishEvent(new PersonEvent("CREATED",person.getId()));
    }
    private record PersonUniqueKey(String name, Double height) {}
}