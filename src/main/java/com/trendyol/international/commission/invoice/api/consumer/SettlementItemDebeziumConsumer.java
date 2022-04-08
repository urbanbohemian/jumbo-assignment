package com.trendyol.international.commission.invoice.api.consumer;

import com.newrelic.api.agent.Trace;
import com.trendyol.international.commission.invoice.api.domain.event.SettlementItemDebeziumMessage;
import com.trendyol.international.commission.invoice.api.service.SettlementItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class SettlementItemDebeziumConsumer {
    private final SettlementItemService settlementItemService;

    @Trace(dispatcher = true)
    @KafkaListener(
            topics = "${kafka-config.consumers[settlement-items-debezium-consumer].topic}",
            groupId = "${kafka-config.consumers[settlement-items-debezium-consumer].props[group.id]}",
            containerFactory = "${kafka-config.consumers[settlement-items-debezium-consumer].factory-bean-name}"
    )
    public void consume(@Payload SettlementItemDebeziumMessage settlementItemDebeziumMessage) {
        try {
            log.info("SettlementItemDebeziumConsumer incoming message: {}", settlementItemDebeziumMessage);
            settlementItemService.process(settlementItemDebeziumMessage);
        } catch (Exception e) {
            log.error("{}",e);
            throw e;
        }
    }
}
