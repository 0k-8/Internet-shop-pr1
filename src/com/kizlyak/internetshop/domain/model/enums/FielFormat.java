package com.kizlyak.internetshop.domain.model.enums;

public enum FielFormat {
    EMAIL("^[A-Za-z0-9+_.-]+@(.+)$"),
    PHONE("^\\+380\\d{9}$"), // Формат для України: +380...
    DATE("dd.MM.yyyy"),
    TEXT_ONLY("^[a-zA-Zа-яА-ЯіІїЇєЄ\\s]*$");
    private final String pattern;

    FielFormat(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
