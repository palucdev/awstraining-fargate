package com.awstraining.backend.business.notifyme.adapter;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.awstraining.backend.business.notifyme.MessageSender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageSnsAWSSender implements MessageSender {

    private static final Logger LOGGER = LogManager.getLogger(MessageSnsAWSSender.class);
    private final AmazonSNS amazonSNS;
    private final String snsTopicArn;
    // TODO: lab1
    //  1. Inject AWS AmazonsSNS from configuration SNSConfig.
    //  2. Make sure that you created new value in parameter store with arn of sns topic.
    //  3. Inject parameter with @Value annotation through constructor.
//    @Autowired
    public MessageSnsAWSSender(AmazonSNS amazonSNS, @Value("${notification.topicarn}") String snsTopicArn) {
        this.amazonSNS = amazonSNS;
        this.snsTopicArn = snsTopicArn;
    }

    @Override
    public void send(String text) {
        PublishRequest publishRequest = new PublishRequest(snsTopicArn, text);
        PublishResult publishResult = amazonSNS.publish(publishRequest);
        LOGGER.info("Message sent to topic {} with message ID {}", snsTopicArn, publishResult.getMessageId());
    }
}
