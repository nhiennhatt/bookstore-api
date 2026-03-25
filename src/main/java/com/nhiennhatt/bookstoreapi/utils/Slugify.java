package com.nhiennhatt.bookstoreapi.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class Slugify {
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");
    private static final Pattern DIACRITICS = Pattern.compile("\\p{InCombiningDiacriticalMarks}+}");
    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^a-zA-Z0-9\\-]");
    private static final Pattern MULTIPLE_HYPHENS = Pattern.compile("-{2,}");
    private static final Pattern TRIM_HYPHENS = Pattern.compile("^-|-$");

    static public String slugify(String name) {
        if (name == null || name.isEmpty()) return "";

        String slug = WHITESPACE.matcher(name).replaceAll("-");
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD);
        slug = DIACRITICS.matcher(slug).replaceAll("");
        slug = NON_ALPHANUMERIC.matcher(slug).replaceAll("");
        slug = MULTIPLE_HYPHENS.matcher(slug).replaceAll("-");
        slug = TRIM_HYPHENS.matcher(slug).replaceAll("");
        return slug.toLowerCase();
    }

    static public String slugify(String name, int padding) {
        if (name.length() > padding) {
            return slugify(name).substring(0, padding);
        } else {
            return slugify(name);
        }
    }
}
