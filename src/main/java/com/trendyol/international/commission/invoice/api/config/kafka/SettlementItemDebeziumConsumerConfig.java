package com.trendyol.international.commission.invoice.api.config.kafka;

import com.trendyol.international.commission.invoice.api.domain.event.SettlementItemDebeziumEvent;
import com.trendyol.international.commission.invoice.api.util.kafka.Consumer;
import com.trendyol.international.commission.invoice.api.util.kafka.KafkaConsumerUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaOperations;

@Configuration
public class SettlementItemDebeziumConsumerConfig {
    private Consumer consumer;
    private final KafkaConsumerUtil kafkaConsumerUtil;

    public SettlementItemDebeziumConsumerConfig(KafkaConsumerUtil kafkaConsumerUtil, KafkaProducerConsumerProps kafkaProducerConsumerProps) {
        this.kafkaConsumerUtil = kafkaConsumerUtil;
        consumer = kafkaProducerConsumerProps.getConsumers().get("settlement-item-debezium-consumer");
    }

    @Bean
    public ConsumerFactory<String, SettlementItemDebeziumEvent> settlementItemDebeziumConsumerFactory() {
        return kafkaConsumerUtil.createConsumerFactory(consumer, SettlementItemDebeziumEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SettlementItemDebeziumEvent> settlementItemDebeziumKafkaListenerContainerFactory(KafkaOperations<String, Object> kafkaOperations) {
        return kafkaConsumerUtil.createSingleKafkaListenerContainerFactory(
                kafkaOperations,
                settlementItemDebeziumConsumerFactory(),
                consumer
        );
    }
}
