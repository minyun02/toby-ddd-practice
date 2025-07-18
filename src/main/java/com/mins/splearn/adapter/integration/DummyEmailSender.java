package com.mins.splearn.adapter.integration;

import com.mins.splearn.application.required.EmailSender;
import com.mins.splearn.domain.Email;

public class DummyEmailSender implements EmailSender {
    @Override
    public void send(Email email, String subject, String body) {
        System.out.println("DummyEmailSender.send: " + email);
    }
}
