package com.ann.prophiuslimitedtask.service;

public interface EmailService {
    void send(String to, String subject, String content);
    String buildEmailVerifyMail(String token);
    String buildResetPasswordMail(String token);
}
