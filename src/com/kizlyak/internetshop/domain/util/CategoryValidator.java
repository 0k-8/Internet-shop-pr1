package com.kizlyak.internetshop.domain.util;

import com.kizlyak.internetshop.domain.Category;
import java.util.ArrayList;
import java.util.List;

public class CategoryValidator {

    public List<ValidationError> validate(Category category) {
        List<ValidationError> errors = new ArrayList<>();

        if (category.getName() == null || category.getName().trim().isEmpty()) {
            errors.add(new ValidationError("name", "Назва категорії не може бути порожньою"));
        }

        return errors;
    }
}