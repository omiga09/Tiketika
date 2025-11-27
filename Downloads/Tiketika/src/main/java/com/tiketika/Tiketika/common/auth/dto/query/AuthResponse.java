package com.tiketika.Tiketika.common.auth.dto.query;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Profile profile;
    private List<String> roles;



    @Data
    @AllArgsConstructor
    public static class Profile {
        private String firstname;
        private String email;
        private String phone;
    }

    // Constructor mpya kwa 4 args
    public AuthResponse( Profile profile, List<String> roles, List<String> authorities) {
        this.profile = profile;
        this.roles = roles;
    }

    }
