package com.kizlyak.internetshop.domain;

import java.util.Map;

public class EntityValidationException extends RuntimeException {

    private final Map<String, String> errors;

    public EntityValidationException(Map<String, String> errors) {
        super("Validation failed for entity");
        this.errors = errors;
    }

    public EntityValidationException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
