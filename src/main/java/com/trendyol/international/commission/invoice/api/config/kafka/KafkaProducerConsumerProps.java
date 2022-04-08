package com.trendyol.international.commission.invoice.api.config.kafka;

import com.trendyol.international.commission.invoice.api.util.Consumer;
import com.trendyol.international.commission.invoice.api.util.Producer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "kafka-config")
public class KafkaProducerConsumerProps {
    private Map<String, String> stretch;
    private Map<String, Consumer> consumers;
    private Map<String, Producer> producers;
    private Map<String, String> integrationTopics;

    public Map<String, String> getStretch() {
        return stretch;
    }

    public void setStretch(Map<String, String> stretch) {
        this.stretch = stretch;
    }

    public Map<String, Consumer> getConsumers() {
        return consumers;
    }

    public void setConsumers(Map<String, Consumer> consumers) {
        this.consumers = consumers;
    }

    public Map<String, Producer> getProducers() {
        return producers;
    }

    public void setProducers(Map<String, Producer> producers) {
        this.producers = producers;
    }

    public Map<String, String> getIntegrationTopics() {
        return integrationTopics;
    }

    public void setIntegrationTopics(Map<String, String> integrationTopics) {
        this.integrationTopics = integrationTopics;
    }
}