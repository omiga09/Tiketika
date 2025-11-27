package com.tiketika.Tiketika.common.auth.dto.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;
}
