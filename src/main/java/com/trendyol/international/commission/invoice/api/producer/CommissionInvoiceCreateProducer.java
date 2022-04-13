package com.trendyol.international.commission.invoice.api.producer;

import com.trendyol.international.commission.invoice.api.config.kafka.KafkaProducerConsumerProps;
import com.trendyol.international.commission.invoice.api.domain.event.CommissionInvoiceCreateMessage;
import com.trendyol.international.commission.invoice.api.util.KafkaSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommissionInvoiceCreateProducer {
    private final KafkaSender kafkaSender;
    private final KafkaProducerConsumerProps kafkaProducerConsumerProps;

    public void produceCommissionInvoiceCreateMessage(CommissionInvoiceCreateMessage commissionInvoiceCreateMessage) {
        kafkaSender.send(
                kafkaProducerConsumerProps.getIntegrationTopics().get("commission-invoice-create-topic"),
                commissionInvoiceCreateMessage.getSellerId().toString(),
                commissionInvoiceCreateMessage
        );
    }
}
