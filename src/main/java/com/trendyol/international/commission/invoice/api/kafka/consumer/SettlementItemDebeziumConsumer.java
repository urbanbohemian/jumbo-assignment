package com.trendyol.international.commission.invoice.api.kafka.consumer;

import com.newrelic.api.agent.Trace;
import com.trendyol.international.commission.invoice.api.domain.event.SettlementItemDebeziumEvent;
import com.trendyol.international.commission.invoice.api.kafka.failover.KafkaConsumerExceptionService;
import com.trendyol.international.commission.invoice.api.service.SettlementItemService;
import com.trendyol.international.commission.invoice.api.util.mapper.SettlementItemMapper;
import com.trendyol.kafkaconfig.annotation.DependsOnKafkaFactories;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Component
@DependsOnKafkaFactories
public class SettlementItemDebeziumConsumer {
    private final SettlementItemService settlementItemService;
    private final KafkaConsumerExceptionService kafkaConsumerExceptionService;

    @Trace(dispatcher = true)
    @KafkaListener(
            topics = "${kafka-config.consumers[settlement-item-debezium-consumer].topic}",
            groupId = "${kafka-config.consumers[settlement-item-debezium-consumer].props[group.id]}",
            containerFactory = "${kafka-config.consumers[settlement-item-debezium-consumer].factory-bean-name}"
    )
    public void consume(@Payload SettlementItemDebeziumEvent message,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
                        @Header(KafkaHeaders.OFFSET) Long offset,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("SettlementItemDebeziumConsumer consumed with topic: {}, and partition: {}, and offset: {}, and message: {}", topic, partition, offset, message);
        try {
            settlementItemService.process(SettlementItemMapper.INSTANCE.settlementItemDto(message.getAfter()));
        } catch (Exception exception) {
            log.error("SettlementItemDebeziumConsumer error occurred: {}", exception.getMessage());
            throw exception;
        }
    }

    @Transactional
    @Trace(dispatcher = true)
    @KafkaListener(
            topics = "${kafka-config.consumers[settlement-item-debezium-consumer].retry-topic}",
            groupId = "${kafka-config.consumers[settlement-item-debezium-consumer].props[retry-group.id]}",
            containerFactory = "${kafka-config.consumers[settlement-item-debezium-consumer].factory-bean-name}"
    )
    public void consumeRetry(@Payload SettlementItemDebeziumEvent message,
                             @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
                             @Header(KafkaHeaders.OFFSET) Long offset,
                             @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("SettlementItemDebeziumRetryConsumer consumed with topic: {}, and partition: {}, and offset: {}, and message: {}", topic, partition, offset, message);
        try {
            settlementItemService.process(SettlementItemMapper.INSTANCE.settlementItemDto(message.getAfter()));
            kafkaConsumerExceptionService.deleteException(message.getHashId());
        } catch (Exception exception) {
            log.error("SettlementItemDebeziumRetryConsumer error occurred: {}", exception.getMessage());
            throw exception;
        }
    }
}
