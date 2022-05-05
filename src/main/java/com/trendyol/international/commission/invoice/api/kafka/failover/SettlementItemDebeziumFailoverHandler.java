package com.trendyol.international.commission.invoice.api.kafka.failover;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendyol.international.commission.invoice.api.domain.entity.KafkaConsumerException;
import com.trendyol.international.commission.invoice.api.domain.event.SettlementItemDebeziumEvent;
import com.trendyol.international.commission.invoice.api.repository.KafkaConsumerExceptionRepository;
import com.trendyol.international.commission.invoice.api.util.JsonSupport;
import com.trendyol.kafkaconfig.kafka.FailoverHandler;
import com.trendyol.kafkaconfig.kafka.dto.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service(value = "SettlementItemDebeziumFailoverHandler")
public class SettlementItemDebeziumFailoverHandler implements FailoverHandler, JsonSupport {

    @Value("${kafka-config.consumers[settlement-item-debezium-consumer].retry-topic}")
    private String retryTopic;

    private final KafkaConsumerExceptionRepository kafkaConsumerExceptionRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void handle(Consumer consumer, ConsumerRecord consumerRecord, Exception exception) {
        Throwable throwable = Optional.ofNullable(exception.getCause()).orElseGet(() -> exception);
        String hashId = fromJson(objectMapper, SettlementItemDebeziumEvent.class, consumerRecord.value()).getHashId();
        KafkaConsumerException kafkaConsumerException = kafkaConsumerExceptionRepository.findById(hashId).orElseGet(() -> KafkaConsumerException.builder()
                .id(hashId)
                .topic(retryTopic)
                .key(consumerRecord.key().toString())
                .content(consumerRecord.value())
                .exceptionType(throwable.getClass().getName().substring(throwable.getClass().getName().lastIndexOf(".") + 1))
                .contentClassType(SettlementItemDebeziumEvent.class.getName())
                .build());
        kafkaConsumerException.setRetryCount(Objects.nonNull(kafkaConsumerException.getRetryCount()) ? kafkaConsumerException.getRetryCount() + 1 : 0);
        kafkaConsumerExceptionRepository.save(kafkaConsumerException);
        log.info("SettlementItemDebeziumFailoverHandler handled successfully error message: {}", exception.getMessage());
    }
}