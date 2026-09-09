package org.zirota.islab1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zirota.islab1.entity.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    Page<Person> findByName(String name, Pageable pageable);


}
