package com.tiketika.engine.common.auth.services.command;


import com.tiketika.engine.common.auth.dto.command.ProfileEditRequest;
import com.tiketika.engine.common.auth.dto.command.UserCreateCommand;
import org.springframework.security.core.Authentication;

public interface UserCommandService {
    void changePasswordByEmailAndOldPassword(String email, String oldPassword, String newPassword);
    void requestPasswordReset(String email);
    void confirmPasswordReset(String email, String otp, String newPassword);
    void deleteAccount(Authentication auth);
    UserCreateCommand editProfile(Authentication auth, ProfileEditRequest req);
    UserCreateCommand getMyProfile(Authentication auth);
}
