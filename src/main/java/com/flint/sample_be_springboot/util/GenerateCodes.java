package com.flint.sample_be_springboot.util;

public class GenerateCodes {

    // student code generation
    public static String generateStudentCode(String lastStudentCode) {

        int nextCodeNum = 1;

        if (lastStudentCode != null) {
            //For taking last 3 digits of previous student code and incrementing it by 1
            String numericPart = lastStudentCode.substring(3);
            nextCodeNum = Integer.parseInt(numericPart) + 1;
        }
        String nextCode = String.format("STD%04d", nextCodeNum); // e.g., STD0006

        return nextCode;
    }

    // Employee code generation
    public static String generateEmployeeCode(String lastEmployeeCode) {

        int nextCodeNum = 1;

        if (lastEmployeeCode != null) {
            //For taking last 3 digits of previous employee code and incrementing it by 1
            String numericPart = lastEmployeeCode.substring(3);
            nextCodeNum = Integer.parseInt(numericPart) + 1;
        }
        String nextCode = String.format("EMP%03d", nextCodeNum); // e.g., EMP006

        return nextCode;
    }

    //Generate student admission code

    public static String generateStudentAdmissionNo(String lastAdmissionNo) {

        int nextAdmissionNo = 1;

        if (lastAdmissionNo != null && !lastAdmissionNo.isBlank()) {

            String numericPart = lastAdmissionNo.replaceAll("\\D+", "");
            nextAdmissionNo = Integer.parseInt(numericPart) + 1;
        }

        return String.format("RGN%04d", nextAdmissionNo);
    }

}
