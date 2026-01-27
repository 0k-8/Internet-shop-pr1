package com.kizlyak.internetshop.domain.validator;

import com.kizlyak.internetshop.domain.model.User;
import com.kizlyak.internetshop.domain.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;

public class UserValidator {

    private final UserRepository userRepository;

    // Конструктор, щоб валідатор міг звертатися до бази (JSON)
    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<ValidationError> validate(User user) {
        List<ValidationError> errors = new ArrayList<>();

        // 1. Перевірка формату Email
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            errors.add(new ValidationError("email", "Некоректний формат email"));
        } else {

            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                errors.add(
                      new ValidationError("email", "Користувач з таким email вже зареєстрований"));
            }
        }

        // 3. Перевірка імені
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            errors.add(new ValidationError("firstName", "Ім'я обов'язкове"));
        }

        // 4. Перевірка прізвища
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            errors.add(new ValidationError("lastName", "Прізвище обов'язкове"));
        }

        return errors;
    }
}