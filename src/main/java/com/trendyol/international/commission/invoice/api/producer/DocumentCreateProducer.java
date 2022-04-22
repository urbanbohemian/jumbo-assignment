package com.trendyol.international.commission.invoice.api.producer;

import com.trendyol.international.commission.invoice.api.config.kafka.producer.KafkaConfigurations;
import com.trendyol.international.commission.invoice.api.domain.event.DocumentCreateEvent;
import com.trendyol.international.commission.invoice.api.util.kafka.KafkaSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DocumentCreateProducer {
    private final KafkaSender kafkaSender;
    private final KafkaConfigurations kafkaConfigurations;

    public void produceDocumentCreateMessage(DocumentCreateEvent documentCreateEvent) {
        kafkaSender.send(
                kafkaConfigurations.getIntegrationTopics().get("document-create-topic"),
                documentCreateEvent.getSellerId().toString(),
                documentCreateEvent
        );
    }
}
