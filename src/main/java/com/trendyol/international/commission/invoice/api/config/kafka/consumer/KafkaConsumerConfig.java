package com.trendyol.international.commission.invoice.api.config.kafka.consumer;

import com.trendyol.international.commission.invoice.api.config.kafka.producer.KafkaConfigurations;
import com.trendyol.international.commission.invoice.api.util.kafka.Consumer;
import com.trendyol.international.commission.invoice.api.util.kafka.KafkaConsumerUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaOperations;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
public class KafkaConsumerConfig {
    private final KafkaConsumerUtil kafkaConsumerUtil;
    private final KafkaConfigurations kafkaConfigurations;
    private final ApplicationContext applicationContext;

    public KafkaConsumerConfig(KafkaConsumerUtil kafkaConsumerUtil,
                               KafkaConfigurations kafkaConfigurations,
                               ApplicationContext applicationContext) {
        this.kafkaConsumerUtil = kafkaConsumerUtil;
        this.kafkaConfigurations = kafkaConfigurations;
        this.applicationContext = applicationContext;
    }

    @Bean("KafkaFactories")
    public Map<String, ConcurrentKafkaListenerContainerFactory<?, ?>> kafkaFactories(KafkaOperations<String, Object> kafkaOperations) {
        return kafkaConfigurations.getConsumers()
                .entrySet()
                .stream()
                .filter(stringConsumerEntry -> !Objects.equals(stringConsumerEntry.getKey(), "default") )
                .map(entry -> {
                    Consumer consumer = entry.getValue();
                    Class<?> consumerClass = kafkaConsumerUtil.getDataClass(consumer);
                    ConsumerFactory<String, ?> consumerFactory = kafkaConsumerUtil.createConsumerFactory(consumer, consumerClass);
                    return Pair.of(entry.getKey(), kafkaConsumerUtil.createListenerFactory(kafkaOperations, consumer, consumerFactory, applicationContext));
                }).collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
    }
}