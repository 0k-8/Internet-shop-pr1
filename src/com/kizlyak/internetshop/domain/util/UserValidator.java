package com.kizlyak.internetshop.domain.util;

import com.kizlyak.internetshop.domain.User;
import java.util.ArrayList;
import java.util.List;

public class UserValidator {

    public List<ValidationError> validate(User user) {
        List<ValidationError> errors = new ArrayList<>();
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            errors.add(new ValidationError("email", "Некоректний email"));
        }
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            errors.add(new ValidationError("firstName", "Ім'я обов'язкове"));
        }
        return errors;
    }
}