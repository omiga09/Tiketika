package com.tiketika.engine.common.auth.utils;

import java.util.regex.Pattern;

public class PasswordPolicy {
        public static final String POLICY_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@&!#$%\\^*()_+\\-={}\\[\\]:;\"'<>,.?/\\\\|~`]).{8,}$";

        private static final Pattern POLICY_PATTERN = Pattern.compile(POLICY_REGEX);

        public static boolean isStrong(String pwd) {
            if (pwd == null) return false;
            return POLICY_PATTERN.matcher(pwd).matches();
        }
    }

