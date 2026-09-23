package org.zirota.islab1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zirota.islab1.entity.AppUser;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser,Long> {

    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username);
}
