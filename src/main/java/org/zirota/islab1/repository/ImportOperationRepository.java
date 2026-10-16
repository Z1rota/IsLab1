package org.zirota.islab1.repository;


import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.zirota.islab1.entity.ImportOperation;
import org.zirota.islab1.entity.ImportStatus;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ImportOperationRepository extends JpaRepository<ImportOperation, Long> {

    List<ImportOperation> findAllByUserUsernameOrderByIdDesc(String username);
    List<ImportOperation> findAllByOrderByIdDesc();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o from ImportOperation o where o.id = :id")
    Optional<ImportOperation> findForUpdate(@Param("id") Long id);

    List<ImportOperation> findAllByStatusAndCreatedAtBefore(ImportStatus status, ZonedDateTime createdAt);
    List<ImportOperation> findAllByStatusAndFileKeyIsNotNull(ImportStatus status);
}
