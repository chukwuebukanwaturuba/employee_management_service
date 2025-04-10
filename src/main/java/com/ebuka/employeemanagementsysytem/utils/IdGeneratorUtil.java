package com.ebuka.employeemanagementsysytem.utils;

import java.util.Random;

public class IdGeneratorUtil {

    public static String generateEmployeeId() {
        String letters = getRandomLetters(2);
        String digits = getRandomDigits(4);
        return letters + digits;
    }

    private static String getRandomLetters(int count) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            result.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return result.toString();
    }

    private static String getRandomDigits(int count) {
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            result.append(random.nextInt(10)); // 0-9
        }
        return result.toString();
    }
}
