package com.trendyol.international.commission.invoice.api.config.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    private final KafkaProducerConsumerProps kafkaProducerConsumerProps;

    public KafkaProducerConfig(KafkaProducerConsumerProps kafkaProducerConsumerProps) {
        this.kafkaProducerConsumerProps = kafkaProducerConsumerProps;
    }

    @Bean
    ProducerFactory<String, Object> producerFactory() {
        // Add kafka consumer correlationid interceptor
        Map<String, Object> producerProps = kafkaProducerConsumerProps.getProducers().get("default").getProps();
//        producerProps.putAll(kafkaProducerConsumerProps.getStretch());
        producerProps.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, KafkaProducerInterceptor.class.getName());

        return new DefaultKafkaProducerFactory<>(producerProps);
    }

    @Bean
    KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    KafkaOperations<String, Object> kafkaOperations() {
        return new KafkaTemplate<>(producerFactory());
    }
}