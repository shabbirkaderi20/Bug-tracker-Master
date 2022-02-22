package com.bugtracker.user.service;

import com.bugtracker.user.constant.Constant;
import com.bugtracker.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    JavaMailSender javaMailSender;

    public Boolean sendEmail(String toEmail, String emailBody, String emailSubject) {

        SimpleMailMessage simpleMailMessage= new SimpleMailMessage();
        simpleMailMessage.setFrom(Constant.EMAIL_FROM);
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setText(emailBody);
        simpleMailMessage.setSubject(emailSubject);

        javaMailSender.send(simpleMailMessage);

        return true;
    }
}
