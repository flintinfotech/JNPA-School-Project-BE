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

    public static String generatePassword(String name, String dob) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Username can't be null");
        }
        if (dob == null || dob.isEmpty()) {
            throw new IllegalArgumentException("Aadhaar card at least contain 4 digits");
        }

        //For username
        String firstPart = name.length() >= 4 ? name.substring(0, 4) : padUsername(name);

        //For taking last 4 digits of Aadhaar Card
        String lastPart = dob.substring(0, 4);

        return firstPart + lastPart;
    }


    private static String padUsername(String username) {
        StringBuilder padded = new StringBuilder(username);
        while (padded.length() < 4) {
            padded.append('X'); // Add 'X' if username is too short
        }
        return padded.toString();
    }

    // student code generation
    public static String generateStudentCode(String lastStudentCode) {

        int nextCodeNum = 1;

        if(lastStudentCode != null) {
            //For taking last 3 digits of previous student code and incrementing it by 1
            String numericPart = lastStudentCode.substring(3);
            nextCodeNum = Integer.parseInt(numericPart) + 1;
        }
        String nextCode = String.format("STD%03d", nextCodeNum); // e.g., STD006

        return nextCode;
    }

}

