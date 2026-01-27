package com.kizlyak.internetshop.service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class MailService {

    private final String username = "kizlakdenis@gmail.com";
    private final String password = "fqlxqtcdgndbfgnt";

    public void sendVerificationCode(String toEmail, String code) {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("🔐 Код підтвердження реєстрації");
            message.setText(
                  "Вітаємо!\n\n" +
                        "Ви розпочали реєстрацію облікового запису.\n" +
                        "Ваш код підтвердження:\n\n" +
                        code + "\n\n" +
                        "⏳ Код дійсний протягом 15 хвилин.\n" +
                        "Якщо ви не запитували цей код — просто проігноруйте лист.\n\n" +
                        "З повагою,\n" +
                        "InternetShop"
            );
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}