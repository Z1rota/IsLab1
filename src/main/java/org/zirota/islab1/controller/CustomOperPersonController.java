package org.zirota.islab1.controller;

import org.springframework.web.bind.annotation.*;
import org.zirota.islab1.dto.NationalityCountDto;
import org.zirota.islab1.entity.Country;
import org.zirota.islab1.entity.Person;
import org.zirota.islab1.service.PersonService;

import java.util.List;

@RestController
@RequestMapping("/api/person/special")
public class CustomOperPersonController {
    private final PersonService personService;

    public CustomOperPersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/group-by-nationality")
    public List<NationalityCountDto> groupByNationality() {
        return personService.groupByNationality();
    }
    @DeleteMapping("/nationality/{nationality}")
    public Integer deleteByNationality(@PathVariable Country nationality) {
        return personService.deleteByNationality(nationality);
    }

    @GetMapping("/min-height")
    public Person getPersonMinHeight() {
        return personService.getMinHeightPerson();
    }

    @GetMapping("/hair-percentage/{color}")
    public Person getPersonHairPercentage(@PathVariable String color) {
        return personService.getMinHeightPerson();
    }

    @GetMapping("/eye-count/{color}")
    public Person getPersonEyeCount(@PathVariable String color) {
        return personService.getMinHeightPerson();
    }
}
