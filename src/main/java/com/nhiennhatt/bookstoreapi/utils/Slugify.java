package com.nhiennhatt.bookstoreapi.utils;

import java.text.Normalizer;
import java.util.UUID;
import java.util.regex.Pattern;

public class Slugify {
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");
    private static final Pattern DIACRITICS = Pattern.compile("\\p{InCombiningDiacriticalMarks}+}");
    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^a-zA-Z0-9\\-]");
    private static final Pattern MULTIPLE_HYPHENS = Pattern.compile("-{2,}");
    private static final Pattern TRIM_HYPHENS = Pattern.compile("^-|-$");

    static public String slugify(String name, int maxLength) {
        if (name == null || name.isEmpty()) {
            return generateFallbackString();
        }

        String suffixUnique = UUID.randomUUID().toString().substring(0, 8);
        int suffixLength = suffixUnique.length() + 1;
        int availableLength = maxLength - suffixLength;
        if (availableLength < 0) {
            throw new IllegalArgumentException("Slug length exceeds the maximum allowed length");
        }

        String slug = WHITESPACE.matcher(name).replaceAll("-");
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD);
        slug = DIACRITICS.matcher(slug).replaceAll("");
        slug = NON_ALPHANUMERIC.matcher(slug).replaceAll("");
        slug = MULTIPLE_HYPHENS.matcher(slug).replaceAll("-");
        slug = TRIM_HYPHENS.matcher(slug).replaceAll("");
        slug = slug.toLowerCase();

        if (slug.length() > availableLength) {
            slug = slug.substring(0, availableLength);
            slug = slug.replaceAll("-$", "");
        }

        if (slug.isEmpty()) {
            return generateFallbackString();
        }

        return slug + "-" + suffixUnique;
    }

    private static String generateFallbackString() {
        return UUID.randomUUID().toString().substring(0, 12);
    }
}
