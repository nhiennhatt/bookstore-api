package com.nhiennhatt.bookstoreapi.utils;

import java.security.SecureRandom;
import java.util.HexFormat;

public class RandomText {
    public static String randomText(int length) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length / 2];
        random.nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }
}
