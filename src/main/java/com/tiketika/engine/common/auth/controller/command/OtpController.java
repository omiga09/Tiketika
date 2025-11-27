package com.tiketika.engine.common.auth.controller.command;

import com.tiketika.engine.common.auth.dto.command.OtpVerifyRequest;
import com.tiketika.engine.common.auth.dto.query.AuthResponse;
import com.tiketika.engine.common.auth.services.command.OtpService;
import com.tiketika.engine.common.auth.entities.User;
import com.tiketika.engine.common.auth.repositories.UserRepository;
import com.tiketika.engine.security.jwtUtil.JwtUtil;
import com.tiketika.engine.common.configuration.CustomerUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
public class OtpController {

    private final UserRepository userRepository;
    private final OtpService otpService;
    private final JwtUtil jwtUtil;
    private final CustomerUserDetailsService customerUserDetailsService;

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerifyRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!otpService.verifyOtp(user, request.getOtp())) {
            return ResponseEntity.badRequest().body("Invalid or expired OTP");
        }

        String displayName = user.getFirstName() + " " + user.getLastName();
        AuthResponse.Profile profile = new AuthResponse.Profile(
                displayName,
                user.getEmail(),
                user.getPhone()
        );

        return ResponseEntity.ok(new AuthResponse(
                profile,
                List.of(user.getUserType().name()),
                Collections.singletonList("Email verified successfully. You can now login using your email and password.")
        ));
    }

}
