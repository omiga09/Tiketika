package com.tiketika.Tiketika.common.auth.controller.command;

import com.tiketika.Tiketika.common.auth.dto.command.OtpVerifyRequest;
import com.tiketika.Tiketika.common.auth.dto.command.ProfileEditRequest;
import com.tiketika.Tiketika.common.auth.dto.command.UserCreateCommand;
import com.tiketika.Tiketika.common.auth.entities.User;
import com.tiketika.Tiketika.common.auth.repositories.UserRepository;
import com.tiketika.Tiketika.common.auth.services.command.OtpService;
import com.tiketika.Tiketika.common.auth.services.command.UserCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserCommandController {

    private final UserCommandService userService;
    private final OtpService otpService;
    private final UserRepository userRepository;

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> editProfile(@Valid @RequestBody ProfileEditRequest req, Authentication auth) {
        UserCreateCommand updated = userService.editProfile(auth, req);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteAccount(Authentication auth) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        // Generate and send OTP for deletion
        otpService.generateAndSendOtpEmailOnly(user);

        return ResponseEntity.ok(Map.of(
                "message", "OTP sent to your email. Please confirm with OTP to delete account."
        ));
    }

    @PostMapping("/confirm-deletion")
    public ResponseEntity<?> confirmDeletion(@RequestBody OtpVerifyRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        if (!otpService.verifyOtp(user, req.getOtp())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid or expired OTP"));
        }

        user.setIsAccountActive(false);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Account deleted successfully"));
    }


}
