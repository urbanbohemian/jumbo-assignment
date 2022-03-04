package com.trendyol.international.commission.invoice.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class KafkaSender {
    private final Logger logger = LoggerFactory.getLogger(KafkaSender.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaSender(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public <T> void send(KafkaTemplate<String, Object> kafkaTemplate, String topic, String key, T message) {
        try {
            kafkaTemplate.send(topic, key, message)
                    .completable()
                    .handle((r, t) -> {
                        if (Optional.ofNullable(t).isPresent()) {
                            logger.error("Sending Kafka Message is failed with the following exception: {}, topic: {}, key: {}, message: {} ", t.getMessage(), topic, key, message);
                            throw new RuntimeException(t);
                        }

                        logger.debug("Sending Kafka Message is successful with the following topic: {}, key: {}, message: {} ", topic, key, message);
                        return r;
                    }).get(30, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            logger.error("Sending Kafka Message is failed with the following exception: {}, topic: {}, key: {}, message: {} ", e.getMessage(), topic, key, message);
            throw new RuntimeException(e);
        }
    }

    public <T> void send(String topic, String key, T message) {
        this.send(kafkaTemplate, topic, key, message);
    }
}
