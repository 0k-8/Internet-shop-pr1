package com.kizlyak.internetshop.domain.util;

public record ValidationError(String field, String message) {

    @Override
    public String toString() {
        return String.format("Поле [%s]: %s", field, message);
    }

    public String getMessage() { // Метод має називатися саме так
        return message;
    }
}