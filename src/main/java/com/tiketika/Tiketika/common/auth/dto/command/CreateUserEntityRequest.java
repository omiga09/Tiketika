package com.tiketika.Tiketika.common.auth.dto.command;

import com.tiketika.Tiketika.common.auth.enums.EntityType;
import com.tiketika.Tiketika.common.auth.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CreateUserEntityRequest {
    private Long userId;
    @NotNull
    private EntityType entityType;
    @NotNull
    private UserType userType;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String phone;
    private Set<Long> roles;
}
