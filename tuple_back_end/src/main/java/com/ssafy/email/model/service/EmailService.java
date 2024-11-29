package com.ssafy.email.model.service;

public interface EmailService {
    void sendVerificationEmail(String email);
    String verifyEmail(String token);
    void sendResetPasswordEmail(String email);
    String resetPassword(String token, String newPassword);
}