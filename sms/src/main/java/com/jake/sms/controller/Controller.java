package com.jake.sms.controller;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.sid}")
    private String sid;

    @GetMapping("/sendSms")
    public void sendSms() {
        Twilio.init(sid, authToken);
        Message.creator(new PhoneNumber("5713820818"), new PhoneNumber("(865) 500-4355"), "It works askim!").create();
    }
}
