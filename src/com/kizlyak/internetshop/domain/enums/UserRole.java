package com.kizlyak.internetshop.domain.enums;

public enum UserRole {
    ADMIN("Адміністратор"),
    CUSTOMER("Клієнт"),
    GUEST("Гість"),
    USER("Користувач");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
