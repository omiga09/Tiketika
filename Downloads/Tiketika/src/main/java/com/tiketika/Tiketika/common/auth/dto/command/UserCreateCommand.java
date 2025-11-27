package com.tiketika.Tiketika.common.auth.dto.command;

import com.tiketika.Tiketika.common.auth.enums.UserType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateCommand {
    private String firstName;
    private String lastName;
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;
    @Pattern(regexp = "\\d{10,15}", message = "Phone must be 10 - 15 digits")
    @Column(unique = true)
    private String phone;
    private UserType userType;
}
