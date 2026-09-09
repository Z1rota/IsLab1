package org.zirota.islab1.controller;


import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zirota.islab1.entity.Person;
import org.zirota.islab1.service.PersonService;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {
    private final PersonService personService;
    public PersonController(PersonService personService) {
        this.personService = personService;
    }
    @GetMapping
    public Page<Person> getPersons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String name

    ) {
        return personService.getAll(page,size,sortBy,direction,name);
    }

    @GetMapping("/{id}")
    public Person getPerson(@PathVariable int id) {
        return personService.getById(id);
    }
    @PostMapping
    public void createPerson(@Valid @RequestBody Person person) {
        personService.create(person);
    }

    @PostMapping("/{id}")
    public void updatePerson(@PathVariable int id, @Valid @RequestBody Person person) {
        personService.update(id, person);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(@PathVariable int id) {
        personService.delete(id);

    }


}
