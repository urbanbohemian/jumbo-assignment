package com.trendyol.international.commission.invoice.api.producer;

import com.trendyol.international.commission.invoice.api.config.kafka.KafkaProducerConsumerProps;
import com.trendyol.international.commission.invoice.api.domain.event.DocumentCreateMessage;
import com.trendyol.international.commission.invoice.api.util.KafkaSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DocumentCreateProducer {
    private final KafkaSender kafkaSender;
    private final KafkaProducerConsumerProps kafkaProducerConsumerProps;

    public void produceDocumentCreateMessage(DocumentCreateMessage documentCreateMessage) {
        kafkaSender.send(
                kafkaProducerConsumerProps.getIntegrationTopics().get("document-create-topic"),
                documentCreateMessage.getSellerId().toString(),
                documentCreateMessage
        );
    }
}
