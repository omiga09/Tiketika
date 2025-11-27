package com.tiketika.engine.common.auth.config;

    import java.security.SecureRandom;

    public class PasswordGenerator {

        private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
        private static final String DIGITS = "0123456789";
        private static final String SPECIAL = "@#$%^&*()-_=+!";

        private static final String ALL = UPPER + LOWER + DIGITS + SPECIAL;
        private static final int PASSWORD_LENGTH = 14;

        private static final SecureRandom random = new SecureRandom();

        public static String generatePassword() {
            StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

            // Ensure at least one character from each group
            password.append(UPPER.charAt(random.nextInt(UPPER.length())));
            password.append(LOWER.charAt(random.nextInt(LOWER.length())));
            password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
            password.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

            // Fill remaining characters
            for (int i = 4; i < PASSWORD_LENGTH; i++) {
                password.append(ALL.charAt(random.nextInt(ALL.length())));
            }

            // Shuffle
            return shuffleString(password.toString());
        }

        private static String shuffleString(String input) {
            char[] chars = input.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                int j = random.nextInt(chars.length);
                char temp = chars[i];
                chars[i] = chars[j];
                chars[j] = temp;
            }
            return new String(chars);
        }
    }

