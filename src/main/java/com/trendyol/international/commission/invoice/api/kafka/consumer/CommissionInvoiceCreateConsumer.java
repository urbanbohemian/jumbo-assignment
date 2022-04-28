package com.trendyol.international.commission.invoice.api.kafka.consumer;

import com.newrelic.api.agent.Trace;
import com.trendyol.international.commission.invoice.api.domain.event.CommissionInvoiceCreateEvent;
import com.trendyol.international.commission.invoice.api.service.CommissionInvoiceService;
import com.trendyol.international.commission.invoice.api.kafka.failover.KafkaConsumerExceptionService;
import com.trendyol.international.commission.invoice.api.util.mapper.CommissionInvoiceCreateMapper;
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
public class CommissionInvoiceCreateConsumer {
    private final CommissionInvoiceService commissionInvoiceService;
    private final KafkaConsumerExceptionService kafkaConsumerExceptionService;

    @Trace(dispatcher = true)
    @KafkaListener(
            topics = "${kafka-config.consumers[commission-invoice-create-consumer].topic}",
            groupId = "${kafka-config.consumers[commission-invoice-create-consumer].props[group.id]}",
            containerFactory = "${kafka-config.consumers[commission-invoice-create-consumer].factory-bean-name}"
    )
    public void consume(@Payload CommissionInvoiceCreateEvent message,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
                        @Header(KafkaHeaders.OFFSET) Long offset,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("CommissionInvoiceCreateConsumer consumed with topic: {}, and partition: {}, and offset: {}, and message: {}", topic, partition, offset, message);
        try {
            commissionInvoiceService.createCommissionInvoiceForSeller(CommissionInvoiceCreateMapper.INSTANCE.commissionInvoiceCreateDto(message));
        } catch (Exception exception) {
            log.warn("CommissionInvoiceCreateConsumer error occurred: {}", exception.getMessage());
            throw exception;
        }
    }

    @Transactional
    @Trace(dispatcher = true)
    @KafkaListener(
            topics = "${kafka-config.consumers[commission-invoice-create-consumer].retry-topic}",
            groupId = "${kafka-config.consumers[commission-invoice-create-consumer].props[retry-group.id]}",
            containerFactory = "${kafka-config.consumers[commission-invoice-create-consumer].factory-bean-name}"
    )
    public void consumeRetry(@Payload CommissionInvoiceCreateEvent message,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
                        @Header(KafkaHeaders.OFFSET) Long offset,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("CommissionInvoiceCreateRetryConsumer consumed with topic: {}, and partition: {}, and offset: {}, and message: {}", topic, partition, offset, message);
        try {
            commissionInvoiceService.createCommissionInvoiceForSeller(CommissionInvoiceCreateMapper.INSTANCE.commissionInvoiceCreateDto(message));
            kafkaConsumerExceptionService.deleteException(message.getHashId());
        } catch (Exception exception) {
            log.warn("CommissionInvoiceCreateRetryConsumer error occurred: {}", exception.getMessage());
            throw exception;
        }
    }
}
