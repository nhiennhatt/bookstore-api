package com.nhiennhatt.bookstoreapi.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StripeUtils {
    public static String extractClientSecret(String clientSecret) {
        Pattern secretPattern = Pattern.compile("(secret_[A-Za-z0-9]+)$");
        Matcher secretMatcher = secretPattern.matcher(clientSecret);
        if (secretMatcher.find()) {
            return secretMatcher.group(1);
        }
        return null;
    }

    public static String extractPiCode(String clientSecret) {
        Pattern piPattern = Pattern.compile("^(pi_[A-Za-z0-9]+)");
        Matcher piMatcher = piPattern.matcher(clientSecret);
        if (piMatcher.find()) {
            return piMatcher.group(1);
        }
        return null;
    }
}
