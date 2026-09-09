package org.zirota.islab1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zirota.islab1.entity.Coordinates;

@Repository
public interface CoordinatesRepository extends JpaRepository<Coordinates, Long> {

}
