package com.tiketika.Tiketika.common.auth.controller.command;

import com.tiketika.Tiketika.common.auth.dto.command.*;
import com.tiketika.Tiketika.common.auth.entities.Otp;
import com.tiketika.Tiketika.common.auth.entities.User;
import com.tiketika.Tiketika.common.auth.entities.UserEntity;
import com.tiketika.Tiketika.common.auth.enums.EntityType;
import com.tiketika.Tiketika.common.auth.enums.UserType;
import com.tiketika.Tiketika.common.auth.repositories.UserEntityRepository;
import com.tiketika.Tiketika.common.auth.repositories.UserRepository;
import com.tiketika.Tiketika.common.auth.services.command.EmailService;
import com.tiketika.Tiketika.common.auth.services.command.OtpService;
import com.tiketika.Tiketika.common.auth.services.command.UserCommandService;
import com.tiketika.Tiketika.security.jwtUtil.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final UserEntityRepository userEntityRepository;
    private final OtpService otpService;
    private final UserCommandService userService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // Register: user supplies password; OTP sent for email verification
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        if (userRepository.findByPhone(request.getPhone()).isPresent()) {
            return ResponseEntity.badRequest().body("Phone number already in use");
        }

        User user = new User();
        user.setFirstName(request.getFirst_name());
        user.setLastName(request.getLast_name());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        try {
            user.setUserType(UserType.valueOf(request.getUser_type().toUpperCase()));
        } catch (Exception e) {
            user.setUserType(UserType.CUSTOMER);
        }

        user.setIsEmailVerified(false);
        user.setIsAccountActive(true);

        userRepository.save(user);

        UserEntity userEntity = new UserEntity();
        userEntity.setUser(user);
        userEntity.setEntityType(EntityType.EVENTS);
        userEntityRepository.save(userEntity);


        Otp otp = otpService.generateAndSendOtpEmailOnly(user);

        emailService.sendEmail(
                request.getEmail(),
                "Verify your Tiketika Account",
                "Hello " + request.getFirst_name() + ",\n\n" +
                        "Your account has been created successfully.\n" +
                        "Please verify your email using this OTP: " + otp.getCode() + "\n\n" +
                        "Thank you,\nTiketika Team"
        );

        return ResponseEntity.ok("User registered successfully. OTP sent to verify email.");
    }

    // Login: direct JWT, no OTP
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));


        if (!user.getIsAccountActive()) {
            return ResponseEntity.status(403).body("Account is deleted or inactive. Please register again.");
        }

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }

        if (!user.getIsEmailVerified()) {
            return ResponseEntity.status(403).body("Please verify your email first.");
        }

        if (user.getIsPasswordExpired() ||
                (user.getExpiryDate() != null && user.getExpiryDate().isBefore(LocalDate.now()))) {
            user.setIsPasswordExpired(true);
            userRepository.save(user);
            return ResponseEntity.status(403).body(
                    "Your password has expired. Please reset your password to continue."
            );
        }

        String jwt = jwtUtil.generateToken(user);
        return ResponseEntity.ok(Map.of("token", jwt));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest req) {
        userService.changePasswordByEmailAndOldPassword(req.getEmail(), req.getOldPassword(), req.getNewPassword());
        return ResponseEntity.ok(Map.of("message","Password changed"));
    }

    @PostMapping("/password-reset/request")
    public ResponseEntity<?> requestReset(@Valid @RequestBody ResetPasswordRequest req) {
        userService.requestPasswordReset(req.getEmail());
        return ResponseEntity.ok(Map.of("message","OTP sent"));
    }

    @PostMapping("/password-reset/confirm")
    public ResponseEntity<?> confirmReset(@Valid @RequestBody PasswordResetRequest req) {
        userService.confirmPasswordReset(req.getEmail(), req.getOtp(), req.getNewPassword());
        return ResponseEntity.ok(Map.of("message","Password updated"));
    }
}
