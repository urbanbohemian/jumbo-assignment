package com.trendyol.international.commission.invoice.api.consumer;

import com.newrelic.api.agent.Trace;
import com.trendyol.international.commission.invoice.api.domain.event.SettlementItemDebeziumMessage;
import com.trendyol.international.commission.invoice.api.model.dto.SettlementItemDto;
import com.trendyol.international.commission.invoice.api.service.SettlementItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class SettlementItemDebeziumConsumer {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final SettlementItemService settlementItemService;

    public SettlementItemDebeziumConsumer(SettlementItemService settlementItemService) {
        this.settlementItemService = settlementItemService;
    }

    @Trace(dispatcher = true)
    @KafkaListener(
            topics = "${kafka-config.consumers[settlement-items-debezium-consumer].topic}",
            groupId = "${kafka-config.consumers[settlement-items-debezium-consumer].props[group.id]}",
            containerFactory = "${kafka-config.consumers[settlement-items-debezium-consumer].factory-bean-name}"
    )
    public void consume(@Payload SettlementItemDebeziumMessage settlementItemDebeziumMessage) {
        logger.info("SettlementItemDebeziumConsumer incoming message: {}", settlementItemDebeziumMessage);
        SettlementItemDto settlementItemDto = SettlementItemDto.fromDebeziumMessage(settlementItemDebeziumMessage.getAfter());
        settlementItemService.process(settlementItemDto);
    }
}