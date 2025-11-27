package com.tiketika.Tiketika.common.auth.dto.command;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;
}
