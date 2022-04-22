package com.trendyol.international.commission.invoice.api.config.kafka.producer;

import com.trendyol.international.commission.invoice.api.util.kafka.Consumer;
import com.trendyol.international.commission.invoice.api.util.kafka.Producer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "kafka-config")
public class KafkaConfigurations {
    private Map<String, Consumer> consumers;
    private Map<String, Producer> producers;
    private Map<String, String> integrationTopics;
}