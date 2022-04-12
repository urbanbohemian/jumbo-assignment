package com.trendyol.international.commission.invoice.api.consumer;

import com.newrelic.api.agent.Trace;
import com.trendyol.international.commission.invoice.api.domain.event.SettlementItemDebeziumMessage;
import com.trendyol.international.commission.invoice.api.mapper.SettlementItemMapper;
import com.trendyol.international.commission.invoice.api.service.SettlementItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
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
    public void consume(@Payload SettlementItemDebeziumMessage settlementItemDebeziumMessage,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
                        @Header(KafkaHeaders.OFFSET) Long offset,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic
                        ) {
        log.info("SettlementItemDebeziumConsumer consumed with topic: {}, and partition: {}, and offset: {}, {}", topic, partition, offset, settlementItemDebeziumMessage);
        settlementItemService.process(SettlementItemMapper.INSTANCE.settlementItemDto(settlementItemDebeziumMessage.getAfter()));
    }
}
