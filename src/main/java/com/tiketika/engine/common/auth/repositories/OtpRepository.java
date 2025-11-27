package com.tiketika.engine.common.auth.repositories;



import com.tiketika.engine.common.auth.entities.Otp;
import com.tiketika.engine.common.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByUser(User user);
    Optional<Otp> findByUserAndCode(User user, String code);

}