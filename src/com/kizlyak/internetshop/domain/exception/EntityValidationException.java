package com.kizlyak.internetshop.domain.exception; // Виправлено назву пакету

import com.kizlyak.internetshop.domain.util.ValidationError;
import java.util.List;

/**
 * Кастомний Exception для помилок валідації.
 */
public class EntityValidationException extends RuntimeException {

    // Список помилок, які спричинили цей Exception
    private final List<ValidationError> errors;

    public EntityValidationException(List<ValidationError> errors) {
        // Передаємо базове повідомлення в конструктор RuntimeException
        super("Помилка валідації сутності: знайдено " + errors.size() + " помилок.");
        this.errors = errors;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }
}