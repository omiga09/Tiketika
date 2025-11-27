package com.tiketika.Tiketika.common.auth.dto.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequest {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String otp;
    @NotBlank
    private String newPassword;

}
