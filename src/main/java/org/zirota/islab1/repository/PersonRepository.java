package org.zirota.islab1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.zirota.islab1.dto.NationalityAdapter;
import org.zirota.islab1.dto.NationalityCountDto;
import org.zirota.islab1.entity.Person;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    Page<Person> findByName(String name, Pageable pageable);

    @Query(value = "SELECT delete_one_by_nationality(:nationality)", nativeQuery = true)
    Integer deleteOneByNationality(@Param("nationality") String nationality);

    @Query(value = "SELECT * FROM get_min_height_person()", nativeQuery = true)
    Person findMinHeightPerson();

    @Query(value = "SELECT nationality, person_count AS personCount FROM group_by_nationality()", nativeQuery = true)
    List<NationalityAdapter> groupByNationality();

    @Query(value = "SELECT hair_color_percentage(:color)", nativeQuery = true)
    Double getHairColorPercentage(@Param("color") String color);

    @Query(value = "SELECT count_by_eye_color(:color)", nativeQuery = true)
    Long countByEyeColor(@Param("color") String color);


}
