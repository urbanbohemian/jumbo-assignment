package com.trendyol.international.commission.invoice.api.consumer;

import com.newrelic.api.agent.Trace;
import com.trendyol.international.commission.invoice.api.domain.event.CommissionInvoiceCreateMessage;
import com.trendyol.international.commission.invoice.api.mapper.CommissionInvoiceCreateMapper;
import com.trendyol.international.commission.invoice.api.service.CommissionInvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class CommissionInvoiceCreateConsumer {
    private final CommissionInvoiceService commissionInvoiceService;

    @Trace(dispatcher = true)
    @KafkaListener(
            topics = "${kafka-config.consumers[commission-invoice-create-consumer].topic}",
            groupId = "${kafka-config.consumers[commission-invoice-create-consumer].props[group.id]}",
            containerFactory = "${kafka-config.consumers[commission-invoice-create-consumer].factory-bean-name}"
    )
    public void consume(@Payload CommissionInvoiceCreateMessage commissionInvoiceCreateMessage) {
        log.info("CommissionInvoiceCreateConsumer incoming message: {}", commissionInvoiceCreateMessage);
        commissionInvoiceService.createCommissionInvoiceForSeller(CommissionInvoiceCreateMapper.INSTANCE.commissionInvoiceCreateDto(commissionInvoiceCreateMessage));
    }
}
