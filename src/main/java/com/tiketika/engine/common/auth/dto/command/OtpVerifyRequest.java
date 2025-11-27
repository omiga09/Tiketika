package com.tiketika.engine.common.auth.dto.command;

import lombok.Getter;
import lombok.Setter;

    @Setter
    @Getter
    public class OtpVerifyRequest {
        private String email;
        private String phone;
        private String otp;
    }
