package com.kizlyak.internetshop.domain.exception;

import com.kizlyak.internetshop.domain.validator.ValidationError;
import java.util.List;
import java.util.stream.Collectors;

public class EntityValidationException extends RuntimeException {

    private final List<ValidationError> errors;

    public EntityValidationException(List<ValidationError> errors) {
        super("Помилка валідації даних");
        this.errors = errors;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    @Override
    public String getMessage() {
        return errors.stream()
              .map(e -> e.field() + ": " + e.message())
              .collect(Collectors.joining("; "));
    }
}