package com.awstraining.backend.business.notifyme.adapter;

import com.awstraining.backend.business.notifyme.NotifyMeDO;
import com.awstraining.backend.business.notifyme.Translator;
import com.amazonaws.services.translate.AmazonTranslate;
import com.amazonaws.services.translate.model.TranslateTextRequest;
import com.amazonaws.services.translate.model.TranslateTextResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TranslatorImpl implements Translator {

    private static final Logger LOGGER = LogManager.getLogger(TranslatorImpl.class);
    private final AmazonTranslate amazonTranslate;

    @Autowired
    public TranslatorImpl(AmazonTranslate amazonTranslate) {
        this.amazonTranslate = amazonTranslate;
    }

    @Override
    public String translate(NotifyMeDO notifyMeDO) {
        TranslateTextRequest request = new TranslateTextRequest()
                .withText(notifyMeDO.text())
                .withSourceLanguageCode(notifyMeDO.sourceLc())
                .withTargetLanguageCode(notifyMeDO.targetLc());

        TranslateTextResult result = amazonTranslate.translateText(request);
        String translatedText = result.getTranslatedText();

        LOGGER.info("Successfully translated message: {}", translatedText);

        return translatedText;
    }
}
