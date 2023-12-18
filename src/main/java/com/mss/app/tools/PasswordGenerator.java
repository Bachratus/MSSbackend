package com.mss.app.tools;

import java.security.SecureRandom;

public class PasswordGenerator {
    private static final String CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&?";
    private static final int PASSWORD_LENGTH = 10;

    public static String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int randomIndex = random.nextInt(CHAR_SET.length());
            password.append(CHAR_SET.charAt(randomIndex));
        }

        return password.toString();
    }
}
