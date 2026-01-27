package com.kizlyak.internetshop.domain.model.enums;

public enum UserRole {
    ADMIN("Адміністратор"),
    USER("Користувач");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
