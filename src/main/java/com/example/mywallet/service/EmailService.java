package com.example.mywallet.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


public interface EmailService {


    public void sendEmail(SimpleMailMessage mailMessage,String email, String confirmationToken);
}
