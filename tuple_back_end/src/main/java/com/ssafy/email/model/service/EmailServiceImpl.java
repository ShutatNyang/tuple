package com.ssafy.email.model.service;

import com.ssafy.email.model.mapper.EmailMapper;
import com.ssafy.util.JwtUtil;
import com.ssafy.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailMapper emailMapper;

    @Override
    public void sendVerificationEmail(String email) {
        String token = UUID.randomUUID().toString();
        emailMapper.saveEmailToken(email, token);
        String link = "http://localhost:5173/email-verify?token=" + token;
        sendEmail(email, "Tu,ple 이메일 인증", "링크를 클릭해 인증을 완료하세요 : " + link);
    }

    @Override
    public String verifyEmail(String token) {
        String email = emailMapper.getEmailByToken(token);
        int isVerifySuccess = emailMapper.authorizeMember(email);
        if (email != null && isVerifySuccess == 1) {
            emailMapper.deleteEmailToken(token);
            return "이메일 인증이 완료되었습니다.";
        } else {
            return "유효하지 않거나 만료된 인증입니다.";
        }
    }

    @Override
    public void sendResetPasswordEmail(String email) {
        String token = UUID.randomUUID().toString();
        emailMapper.savePasswordResetToken(email, token);
        String link = "http://localhost:5173/reset-password?token=" + token;
        sendEmail(email, "Tu,ple", "비밀번호 재설정 링크 : " + link);
    }

    @Override
    public String resetPassword(String token, String newPassword) {
        String email = emailMapper.getEmailByPasswordResetToken(token);
        String hashedPassword = PasswordUtil.hashPassword(newPassword);
        if (email != null) {
            emailMapper.updatePassword(email, hashedPassword);
            emailMapper.deletePasswordResetToken(token);
            return "비밀번호 변경이 완료되었습니다.!";
        } else {
            return "유효하지 않거나 만료되었습니다.";
        }
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}