package com.trendyol.international.commission.invoice.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendyol.international.commission.invoice.api.model.KafkaConsumerException;
import com.trendyol.international.commission.invoice.api.repository.KafkaConsumerExceptionRepository;
import com.trendyol.international.commission.invoice.api.util.Hashing;
import com.trendyol.international.commission.invoice.api.util.kafka.FailoverHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class ConsumerFailoverHandler implements FailoverHandler {
    private final ObjectMapper objectMapper;
    private final KafkaConsumerExceptionRepository kafkaConsumerExceptionRepository;

    @Override
    public void handle(ConsumerRecord consumerRecord, Exception exception, String dataClass) {
        try {
            String contentAsJson = objectMapper.writeValueAsString(consumerRecord.value());

            KafkaConsumerException kafkaConsumerException = KafkaConsumerException.builder()
                    .id(Hashing.md5(contentAsJson))
                    .topic(consumerRecord.topic())
                    .key(consumerRecord.key().toString())
                    .content(contentAsJson)
                    .contentClassType(dataClass)
                    .exceptionType(Optional.ofNullable(exception.getCause())
                            .map(cause -> cause.getClass().getName().substring(cause.getClass().getName().lastIndexOf(".") + 1))
                            .orElseGet(() -> exception.getClass().getName().substring(exception.getClass().getName().lastIndexOf(".") + 1)))
                    .build();

            kafkaConsumerExceptionRepository.save(kafkaConsumerException);

            log.error("ConsumerFailoverHandler has handled an exception successfully. The exception is : {}", kafkaConsumerException);
        } catch (Exception e) {
            log.error("ConsumerFailoverHandler has an error while handling retry failover. Message: {}", e.getMessage(), e);
        }
    }
}