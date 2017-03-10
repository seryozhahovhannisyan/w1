package com.connectto.general.model.lcp;

import java.util.Locale;

public enum Language {

    ARMENIAN(1, "hy", "Armenian", new Locale("hy")),
    ENGLISH(2, "en", "English", Locale.ENGLISH),
    RUSSIAN(3, "ru", "Russian", new Locale("ru")),
    FRANCE(4, "fr", "France", Locale.FRANCE),
    SPANISH(5, "es", "Spanish", new Locale("es")),
    PERSIAN(6, "fa", "Persian", new Locale("fa"));

    Language(final int value, final String key, final String title, final Locale locale) {
        this.value = value;
        this.key = key;
        this.title = title;
        this.locale = locale;
    }

    public static Language getDefault() {
        return ENGLISH;
    }

    public static synchronized Language valueOf(final int value) {
        for (Language e : Language.values()) {
            if (e.value == value) {
                return e;
            }
        }
        return getDefault();
    }

    public static synchronized Language localeOf(Locale locale) {
        for (Language e : Language.values()) {
            if (e.locale == locale) {
                return e;
            }
        }
        return getDefault();
    }

    public static Language languageOf(final String language) {
        for (Language e : Language.values()) {
            if (e.locale.getLanguage().equalsIgnoreCase(language)) {
                return e;
            }
        }
        return getDefault();
    }

    public static Language keyOf(final String key) {
        for (Language e : Language.values()) {
            if (e.key.equalsIgnoreCase(key)) {
                return e;
            }
        }
        return getDefault();
    }

    private final int value;
    private final String key;
    private final String title;
    private final Locale locale;

    public int getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public Locale getLocale() {
        return locale;
    }
}
