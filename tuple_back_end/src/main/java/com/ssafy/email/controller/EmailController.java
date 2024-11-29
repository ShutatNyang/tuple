package com.ssafy.email.controller;

import com.ssafy.email.model.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/send-verification")
    public String sendVerificationEmail(@RequestParam String email) {
        emailService.sendVerificationEmail(email);
        return "Verification email sent to " + email;
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token") String token) {
        return emailService.verifyEmail(token);
    }

    @PostMapping("/send-reset-password")
    public String sendResetPasswordEmail(@RequestParam String email) {
        emailService.sendResetPasswordEmail(email);
        return "Password reset email sent to " + email;
    }
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        return emailService.resetPassword(token, newPassword);
    }
}