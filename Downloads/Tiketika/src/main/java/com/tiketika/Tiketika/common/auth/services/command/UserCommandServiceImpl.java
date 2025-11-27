package com.tiketika.Tiketika.common.auth.services.command;


import com.tiketika.Tiketika.common.auth.dto.command.ProfileEditRequest;
import com.tiketika.Tiketika.common.auth.dto.command.UserCreateCommand;
import com.tiketika.Tiketika.common.auth.entities.User;
import com.tiketika.Tiketika.common.auth.enums.UserType;
import com.tiketika.Tiketika.common.auth.repositories.UserRepository;
import com.tiketika.Tiketika.common.auth.utils.PasswordPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void changePasswordByEmailAndOldPassword(String email, String oldPwd, String newPwd) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No account for that email"));

        if (!passwordEncoder.matches(oldPwd, user.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old password incorrect");

        if (!PasswordPolicy.isStrong(newPwd))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password does not meet policy");

        if (passwordEncoder.matches(newPwd, user.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password must be different from old password");

        user.setPassword(passwordEncoder.encode(newPwd));
        user.setIsPasswordExpired(false);
        user.setPasswordChangedAt(Instant.now());
        userRepository.save(user);
    }


    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No account for that email"));


        otpService.generateAndSendOtp(user);
    }


    @Override
    public void confirmPasswordReset(String email, String otp, String newPwd) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No account for that email"));

        boolean validOtp = otpService.verifyOtp(user, otp);
        if (!validOtp)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired OTP");

        if (!PasswordPolicy.isStrong(newPwd))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password does not meet policy");

        user.setPassword(passwordEncoder.encode(newPwd));
        user.setIsPasswordExpired(false);
        user.setPasswordChangedAt(Instant.now());
        userRepository.save(user);
    }

    public void deleteAccount(Authentication auth) {
        String email = auth.getName(); // usually email or username stored in Authentication
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        if (user.getUserType() != UserType.CUSTOMER)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only customers may request delete");

        user.setIsAccountActive(false); // soft delete
        userRepository.save(user);
    }


    public UserCreateCommand editProfile(Authentication auth, ProfileEditRequest req) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        // Allow all users (including CUSTOMER) to edit their profile
        if (req.getFirstName() != null) user.setFirstName(req.getFirstName());
        if (req.getLastName() != null) user.setLastName(req.getLastName());
        if (req.getEmail() != null) user.setEmail(req.getEmail());
        if (req.getPhone() != null) user.setPhone(req.getPhone());

        // apply other editable fields if any
        userRepository.save(user);
        return mapToDto(user);
    }



    private UserCreateCommand mapToDto(User user) {
        // map User -> UserDto
        UserCreateCommand dto = new UserCreateCommand();
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhone());
        dto.setUserType(user.getUserType());
        return dto;
    }
    public UserCreateCommand getMyProfile(Authentication auth) {

        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Convert to your DTO (UserCreateCommand)
        return UserCreateCommand.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .userType(user.getUserType())
                .build();
    }

}
