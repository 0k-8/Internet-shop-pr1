package com.kizlyak.internetshop.service;

import java.util.Random;

public class VerificationService {

    private final MailService mailService;
    private final Random random = new Random();

    public VerificationService() {
        this.mailService = new MailService();
    }

    // Генерує 6-значний цифровий код
    public String generateCode() {
        return String.format("%06d", random.nextInt(1000000));
    }

    // Відправляє код через MailService
    public void sendCode(String email, String code) {
        mailService.sendVerificationCode(email, code);
    }
}