package com.connectto.general.util;

import java.util.Random;
import java.util.UUID;

/**
 * Created by htdev001 on 2/14/14.
 */
public class Generator {

    private static final char[] ALPHA_UPPERS = "ABCDEFGHILMNOPQRSTUVWYZ".toCharArray();
    private static final char[] ALPHA_LOWERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] ALPHA_UPPER_NUMERICS = "ABCDEFGHILMNOPQRSTUVWYZ0123456789".toCharArray();
    private static final char[] ALPHA_NUMERICS = "ABCDEFGHILMNOPQRSTUVWYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

    private static final char[] NUMERICS = "0123456789".toCharArray();

    public static synchronized String generateRandomString() {

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 32; i++) {
            char c = ALPHA_NUMERICS[random.nextInt(ALPHA_NUMERICS.length)];
            sb.append(c);
        }
        String output = sb.toString();

        return output;
    }

    public static synchronized String generateRandomString(int length) {

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char c = ALPHA_LOWERS[random.nextInt(ALPHA_LOWERS.length)];
            sb.append(c);
        }
        String output = sb.toString();

        return output;
    }

    public static synchronized String generateAlphaNumeric(int length) {

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char c = ALPHA_NUMERICS[random.nextInt(ALPHA_NUMERICS.length)];
            sb.append(c);
        }
        String output = sb.toString();

        return output;
    }

    public static synchronized String generateSessionId() {

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 32; i++) {
            char c = ALPHA_UPPER_NUMERICS[random.nextInt(ALPHA_UPPER_NUMERICS.length)];
            sb.append(c);
        }
        String output = sb.toString();

        return output;
    }

    public static synchronized String generateNumeric() {

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            char c = NUMERICS[random.nextInt(NUMERICS.length)];
            sb.append(c);
        }
        String output = sb.toString();

        return output;
    }

    public static synchronized String generateNumeric(int count) {

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            char c = NUMERICS[random.nextInt(NUMERICS.length)];
            sb.append(c);
        }
        String output = sb.toString();

        return output;
    }

    public static synchronized String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static synchronized String generateLanguageStoreKey(String langKey, int partitionId) {
        return String.format("%s_%d",langKey,partitionId);
    }

}
