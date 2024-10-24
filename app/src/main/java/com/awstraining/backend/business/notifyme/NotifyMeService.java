package com.awstraining.backend.business.notifyme;

import com.amazonaws.services.translate.model.TranslateTextRequest;

import org.springframework.stereotype.Service;

@Service
public class NotifyMeService {

    // TODO: lab1
    MessageSender messageSender;

    Translator translator;

    // TODO lab2
    //  1. Inject Translator
    // TODO lab3
    //  1. Inject sentiment detector
    //    @Autowired
    public NotifyMeService(MessageSender messageSender,
                           Translator translator) {
        this.messageSender = messageSender;
        this.translator = translator;
    }

    public String notifyMe(NotifyMeDO notifyMe) {
        // TODO: lab1
        //  1. Send text using sender.
        this.messageSender.send(notifyMe.text());
        //  2. Return sent message.
        // TODO: lab2
        //  1. Translate text from using translator.
        TranslateTextRequest request = new TranslateTextRequest()
                .withText(notifyMe.text())
                .withSourceLanguageCode(notifyMe.sourceLc())
                .withTargetLanguageCode(notifyMe.targetLc());
        //  2. Change sending of text to "translated text" and return it.
        return this.translator.translate(notifyMe);
        // TODO: lab3
        //  1. Detect sentiment of translated message.
        //  2. Change sending of text to "setiment: translated text" and return it.
        //        return "This service is not available.";
    }
}
