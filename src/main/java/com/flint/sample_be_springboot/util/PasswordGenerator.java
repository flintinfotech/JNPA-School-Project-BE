package com.flint.sample_be_springboot.util;


import java.security.SecureRandom;

public class PasswordGenerator {
    private static final String DIGITS = "0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generatePassword(String name) {
        // Ensure username has at least 4 characters
        String firstPart = name.length() >= 4 ? name.substring(0, 4) : padUsername(name);

        // Generate 4 random digits
        StringBuilder digitsPart = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            digitsPart.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        }

        return firstPart + digitsPart;
    }

    public static String generatePassword(String name, String aadhaarCard) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Username can't be null");
        }
        if (aadhaarCard == null || aadhaarCard.isEmpty()) {
            throw new IllegalArgumentException("Aadhaar card at least contain 4 digits");
        }

        //For username
        String firstPart = name.length() >= 4 ? name.substring(0, 4) : padUsername(name);

        //For taking last 4 digits of Aadhaar Card
        String lastPart = aadhaarCard.substring(aadhaarCard.length() - 4);

        return firstPart + lastPart;
    }


    private static String padUsername(String username) {
        StringBuilder padded = new StringBuilder(username);
        while (padded.length() < 4) {
            padded.append('X'); // Add 'X' if username is too short
        }
        return padded.toString();
    }
}

