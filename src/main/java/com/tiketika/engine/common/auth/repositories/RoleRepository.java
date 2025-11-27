package com.tiketika.engine.common.auth.repositories;

import com.tiketika.engine.common.auth.entities.Role;
import com.tiketika.engine.common.auth.enums.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // chukua role ya kwanza tu ikiwa kuna duplicates
    Optional<Role> findFirstByName(String name);

    // rudisha list yote ikiwa kuna duplicates
    List<Role> findAllByName(String name);

    // combination ya name + entityType
    Optional<Role> findFirstByNameAndEntityType(String name, EntityType entityType);

    List<Role> findAllByNameAndEntityType(String name, EntityType entityType);
}
