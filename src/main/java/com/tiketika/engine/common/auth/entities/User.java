package com.tiketika.engine.common.auth.entities;

import com.tiketika.engine.common.auditing.BaseAuditable;
import com.tiketika.engine.common.auth.enums.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@Table(
        name = "app_user",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email"),
                @Index(name = "idx_user_phone", columnList = "phone"),
                @Index(name = "idx_user_is_active", columnList = "is_account_active")
        }
)
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50)
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50)
    @Column(name = "last_name")
    private String lastName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{10,15}", message = "Phone must be 10 - 15 digits")
    @Column(unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "user_type")
    private UserType userType;

    @Column(name = "is_email_verified", columnDefinition = "TINYINT(1)")
    private Boolean isEmailVerified = false;

    @Column(name = "is_account_active", columnDefinition = "TINYINT(1)")
    private Boolean isAccountActive = true;

    @Min(value = 0)
    private int password_attempts;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean is_account_locked = false;

    @Column(name = "is_password_expired", columnDefinition = "TINYINT(1)")
    private Boolean isPasswordExpired = false;

    @Column(name = "password_changed_at")
    private Instant passwordChangedAt;

    private LocalDate expiryDate;
}
