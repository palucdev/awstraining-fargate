package com.awstraining.backend.business.notifyme;

import org.springframework.stereotype.Service;

@Service
public class NotifyMeService {

    // TODO: lab1
    MessageSender messageSender;

    // TODO lab2
    //  1. Inject Translator
    // TODO lab3
    //  1. Inject sentiment detector
    //    @Autowired
    public NotifyMeService(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public String notifyMe(NotifyMeDO notifyMe) {
        // TODO: lab1
        //  1. Send text using sender.
        this.messageSender.send(notifyMe.text());
        //  2. Return sent message.
        return notifyMe.text();
        // TODO: lab2
        //  1. Translate text from using translator.
        //  2. Change sending of text to "translated text" and return it.
        // TODO: lab3
        //  1. Detect sentiment of translated message.
        //  2. Change sending of text to "setiment: translated text" and return it.
        //        return "This service is not available.";
    }
}
