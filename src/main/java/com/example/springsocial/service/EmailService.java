package com.example.springsocial.service;

import org.springframework.stereotype.Service;

import com.example.springsocial.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    
    @Autowired
    private UserRepository userRepository;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendTemporaryPasswordEmail(String to, String temporaryPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Temporary Password");
        message.setText("Your temporary password is: " + temporaryPassword);
        javaMailSender.send(message);
    }
    
    public boolean checkEmail(String email) {
    	return userRepository.existsByEmail(email);
    }
}
