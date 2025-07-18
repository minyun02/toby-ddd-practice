package com.mins.splearn.application.required;

import com.mins.splearn.domain.Email;
import org.springframework.stereotype.Component;

/*
    이메일을 발송한다
 */
@Component
public interface EmailSender {
    void send(Email email, String subject, String body);
}
