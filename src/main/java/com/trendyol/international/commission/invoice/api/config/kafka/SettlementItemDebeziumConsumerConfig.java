package com.trendyol.international.commission.invoice.api.config.kafka;

import com.trendyol.international.commission.invoice.api.domain.event.SettlementItemDebeziumMessage;
import com.trendyol.international.commission.invoice.api.util.Consumer;
import com.trendyol.international.commission.invoice.api.util.KafkaConsumerUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class SettlementItemDebeziumConsumerConfig {
    private Consumer consumer;
    private final KafkaConsumerUtil kafkaConsumerUtil;

    public SettlementItemDebeziumConsumerConfig(KafkaConsumerUtil kafkaConsumerUtil, KafkaProducerConsumerProps kafkaProducerConsumerProps) {
        this.kafkaConsumerUtil = kafkaConsumerUtil;
        consumer = kafkaProducerConsumerProps.getConsumers().get("settlement-item-debezium-consumer");
    }

    @Bean
    public ConsumerFactory<String, SettlementItemDebeziumMessage> settlementItemDebeziumConsumerFactory() {
        return kafkaConsumerUtil.createConsumerFactory(consumer, SettlementItemDebeziumMessage.class);
    }

    @Bean
    public RetryTemplate settlementItemDebeziumConsumerRetryTemplate() {
        return kafkaConsumerUtil.createRetryTemplate(consumer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SettlementItemDebeziumMessage> settlementItemDebeziumKafkaListenerContainerFactory(KafkaOperations<String, Object> kafkaOperations) {
        return kafkaConsumerUtil.createSingleKafkaListenerContainerFactory(
                kafkaOperations,
                settlementItemDebeziumConsumerFactory(),
                consumer,
                settlementItemDebeziumConsumerRetryTemplate()
        );
    }
}
