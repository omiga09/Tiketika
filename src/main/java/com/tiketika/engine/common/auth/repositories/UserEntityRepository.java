package com.tiketika.engine.common.auth.repositories;

import com.tiketika.engine.common.auth.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
}
