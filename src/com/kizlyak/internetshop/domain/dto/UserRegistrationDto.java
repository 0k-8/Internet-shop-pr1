package com.kizlyak.internetshop.domain.dto;

import com.kizlyak.internetshop.domain.exception.EntityValidationException;
import com.kizlyak.internetshop.domain.validator.ValidationError;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO для реєстрації нового користувача
 */
public record UserRegistrationDto(
      String username,
      String email,
      String password,
      String firstName,
      String lastName
) {

    public UserRegistrationDto {
        List<ValidationError> errors = new ArrayList<>();

        if (username == null || username.isBlank()) {
            errors.add(new ValidationError("username", "Логін не може бути порожнім"));
        }
        if (password == null || password.length() < 8) {
            errors.add(new ValidationError("password", "Пароль має бути не менше 8 символів"));
        }
        if (email == null || !email.contains("@")) {
            errors.add(new ValidationError("email", "Некоректний формат email"));
        }

        if (!errors.isEmpty()) {
            throw new EntityValidationException(errors);
        }
    }

}