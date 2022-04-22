package com.trendyol.international.commission.invoice.api.producer;

import com.trendyol.international.commission.invoice.api.domain.event.CommissionInvoiceCreateEvent;
import com.trendyol.kafkaconfig.config.KafkaConfigurations;
import com.trendyol.kafkaconfig.kafka.KafkaSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommissionInvoiceCreateProducer {
    private final KafkaSender kafkaSender;
    private final KafkaConfigurations kafkaConfigurations;

    public void produceCommissionInvoiceCreateMessage(CommissionInvoiceCreateEvent commissionInvoiceCreateEvent) {
        kafkaSender.send(
                kafkaConfigurations.getIntegrationTopics().get("commission-invoice-create-topic"),
                commissionInvoiceCreateEvent.getSellerId().toString(),
                commissionInvoiceCreateEvent
        );
    }
}
