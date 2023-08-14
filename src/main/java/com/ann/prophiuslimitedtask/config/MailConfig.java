package com.ann.prophiuslimitedtask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com"); // Set your SMTP host
        mailSender.setPort(587); // Set your SMTP port
        mailSender.setUsername("your-gmail-email@gmail.com"); // Set your SMTP username
        mailSender.setPassword("your-gmail-password"); // Set your SMTP password

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
/* This configuration class is responsible for configuring the JavaMailSender, which is used for sending emails.
It sets up the properties for the SMTP server, such as the host, port, username, and password.
These properties are necessary to establish a connection with the SMTP server and send emails.
 */