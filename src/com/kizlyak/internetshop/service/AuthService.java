package com.kizlyak.internetshop.service;

import com.kizlyak.internetshop.domain.dto.UserRegistrationDto;
import com.kizlyak.internetshop.domain.model.User;
import com.kizlyak.internetshop.domain.model.enums.UserRole;
import com.kizlyak.internetshop.domain.validator.UserValidator;
import com.kizlyak.internetshop.domain.validator.ValidationError;
import com.kizlyak.internetshop.infrastructure.UnitOfWork;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.mindrot.bcrypt.BCrypt;


public class AuthService {

    private final UnitOfWork uow;
    private final UserValidator userValidator;
    private final VerificationService verificationService; // Новий сервіс
    private final ConcurrentHashMap<String, VerificationData> verificationStorage = new ConcurrentHashMap<>();

    public AuthService(UnitOfWork uow) {
        this.uow = uow;
        this.userValidator = new UserValidator(uow.getUserRepository());
        this.verificationService = new VerificationService(); // Ініціалізація
    }

    public void startRegistration(String email) {
        // Використовуємо новий сервіс
        String code = verificationService.generateCode();

        // Зберігаємо код і дедлайн (15 хв)
        verificationStorage.put(email,
              new VerificationData(code, LocalDateTime.now().plusMinutes(15), false));

        // Відправляємо реально через сервіс
        verificationService.sendCode(email, code);

        System.out.println("[System] Код підтвердження надіслано на " + email);
    }

    public boolean confirmCode(String email, String userInputCode) {
        VerificationData data = verificationStorage.get(email);

        if (data == null) {
            throw new RuntimeException("Запит на підтвердження не знайдено.");
        }

        if (LocalDateTime.now().isAfter(data.expiryTime())) {
            verificationStorage.remove(email);
            throw new RuntimeException("Час дії коду вичерпано (15 хв)!");
        }

        if (!data.code().equals(userInputCode)) {
            throw new RuntimeException("Невірний код підтвердження!");
        }

        // Позначаємо як верифікований
        verificationStorage.put(email, new VerificationData(data.code(), data.expiryTime(), true));
        return true;
    }

    public void register(UserRegistrationDto dto) {
        VerificationData data = verificationStorage.get(dto.email());

        if (data == null || !data.isVerified()) {
            throw new RuntimeException("Email не підтверджено!");
        }

        String hashedPassword = BCrypt.hashpw(dto.password(), BCrypt.gensalt());

        User newUser = new User(
              dto.username(),
              dto.email(),
              hashedPassword,
              dto.firstName(),
              dto.lastName(),
              UserRole.USER
        );

        // Повна валідація об'єкта
        List<ValidationError> errors = userValidator.validate(newUser);
        if (!errors.isEmpty()) {
            throw new RuntimeException("Помилка: " + errors.get(0).message());
        }

        uow.getUserRepository().save(newUser);
        uow.commit();

        verificationStorage.remove(dto.email());
    }

    public User login(String email, String password) {

        User user = uow.getUserRepository().findByEmail(email)
              .orElseThrow(() -> new RuntimeException("Користувача з таким email не знайдено!"));

        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new RuntimeException("Невірний пароль!");
        }

        return user;
    }

    private record VerificationData(String code, LocalDateTime expiryTime, boolean isVerified) {

    }
}