package org.zirota.islab1.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zirota.islab1.entity.ImportOperation;

import java.util.List;

@Repository
public interface ImportOperationRepository extends JpaRepository<ImportOperation, Long> {

    List<ImportOperation> findAllByUserUsernameOrderByIdDesc(String username);
    List<ImportOperation> findAllByOrderByIdDesc();
}
