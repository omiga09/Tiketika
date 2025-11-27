package com.tiketika.engine.common.auth.repositories;

import com.tiketika.engine.common.auth.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findFirstByName(String name);
    List<Permission> findAllByName(String name);
}
