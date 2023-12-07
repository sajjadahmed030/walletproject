package com.example.mywallet.service.impl;

import com.example.mywallet.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private JavaMailSender javaMailSender;


    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    @Async
    public void sendEmail(SimpleMailMessage mailMessage, String email, String confirmationToken) {
        mailMessage.setTo(email);
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("officesajjadahmed@gmail.com");
        mailMessage.setText("To confirm your account, please click here : "
                +"http://localhost:8080/user/auth/confirm-account?token="+confirmationToken);


        javaMailSender.send(mailMessage);
    }
}
