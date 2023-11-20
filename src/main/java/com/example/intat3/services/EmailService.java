package com.example.intat3.services;

import com.example.intat3.Entity.Announcement;
import com.example.intat3.Entity.EmailAddress;

import java.util.List;


public interface EmailService {
    String sendOTPMail(String email, String subject, String text);

    String OTPCheck(String email, String otp, Integer catId);

    List<EmailAddress> getListCat(String email);

    void unSubscribe(String email, Integer catId);
//    String sendNotification(String email, String subject, String text);
    void sendEmailAfterUpdate(Announcement announcement);
}
