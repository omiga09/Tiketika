package com.tiketika.engine.common.auth.dto.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileEditRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
