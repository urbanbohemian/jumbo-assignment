package com.trendyol.international.commission.invoice.api.util.kafka;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendyol.international.commission.invoice.api.config.kafka.producer.KafkaConfigurations;
import com.trendyol.international.commission.invoice.api.service.failoverHandler.FailoverHandler;
import com.trendyol.international.commission.invoice.api.util.JsonSupport;
import com.trendyol.international.commission.invoice.api.util.SpringContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumerUtil implements JsonSupport {
    private final KafkaConfigurations kafkaConfigurations;
    private final ObjectMapper objectMapper;

    public <T> ConsumerFactory<String, T> createConsumerFactory(Consumer consumer, Class<T> classT) {
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);
        var objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ErrorHandlingDeserializer<String> keyDeserializer = new ErrorHandlingDeserializer<>(new StringDeserializer());
        var jsonDeserializer = new JsonDeserializer<>(classT, objectMapper);
        jsonDeserializer.setTypeMapper(typeMapper);
        var valueDeserializer = new ErrorHandlingDeserializer<>(jsonDeserializer);
        Map<String,Object> defaultProps = Optional.ofNullable(kafkaConfigurations.getConsumers()).map(stringConsumerMap -> stringConsumerMap.get("default")).map(Consumer::getProps).orElseGet(HashMap::new);
        Map<String, Object> consumerProps = consumer.getProps();
        defaultProps.putAll(consumerProps);
        return new DefaultKafkaConsumerFactory<>(defaultProps, keyDeserializer, valueDeserializer);
    }

    public void defaultFailoverStrategy(KafkaOperations<String, Object> kafkaOperations, Consumer consumer, ConsumerRecord consumerRecord) {
        try {
            Optional.ofNullable(consumer.getErrorTopic()).ifPresent(errorTopic -> kafkaOperations.send(consumer.getErrorTopic(), consumerRecord.key().toString(), consumerRecord.value()));
        } catch (Exception e) {
            log.error("Consumer Failover has an error while sending error to error topic. topic: {}, key: {}, val: {}",
                    consumer.getErrorTopic(),
                    consumerRecord.key(),
                    asJson(objectMapper, consumerRecord.value())
            );
        }
    }

    public void handleFailover(KafkaOperations<String, Object> kafkaOperations, Consumer consumer, ConsumerRecord record, Exception exception) {
        try {
            Optional.ofNullable(consumer.getFailoverHandlerBeanName()).ifPresent(failoverHandlerBeanName -> {
                FailoverHandler failoverService = SpringContext.context.getBean(failoverHandlerBeanName, FailoverHandler.class);
                failoverService.handle(consumer, record, exception);
            });
        } catch (Exception e) {
            defaultFailoverStrategy(kafkaOperations, consumer, record);
        }
    }

    public <T> ConcurrentKafkaListenerContainerFactory<String, T> createKafkaListenerContainerFactory(
            KafkaOperations<String, Object> kafkaOperations,
            ConsumerFactory<String, T> consumerFactory,
            Consumer consumer
    ) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setMissingTopicsFatal(Optional.ofNullable(consumer.getMissingTopicAlertEnable()).orElse(false));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        factory.getContainerProperties().setSyncCommits(Optional.of(consumer.getSyncCommit()).orElse(true));
        factory.getContainerProperties().setSyncCommitTimeout(Duration.ofSeconds(Optional.of(consumer.getSyncCommitTimeoutSecond()).orElse(5)));
        factory.setConcurrency(Optional.of(consumer.getConcurrency()).orElse(1));
        factory.setBatchListener(false);
        factory.setAutoStartup(Optional.ofNullable(consumer.getAutoStartup()).orElse(true));
        ExponentialBackOffWithMaxRetries exponentialBackOffWithMaxRetries = new ExponentialBackOffWithMaxRetries(Optional.of(consumer.getRetryCount()).orElse(0));
        exponentialBackOffWithMaxRetries.setInitialInterval(Optional.ofNullable(consumer.getBackoffIntervalMillis()).orElse(1000L));
        exponentialBackOffWithMaxRetries.setMultiplier(Optional.ofNullable(consumer.getMultiplier()).orElse(2.0));
        exponentialBackOffWithMaxRetries.setMaxInterval(Optional.ofNullable(consumer.getMaxInterval()).orElse(1000_000L));
        factory.setCommonErrorHandler(new DefaultErrorHandler((record, exception) -> handleFailover(kafkaOperations, consumer, record, exception), exponentialBackOffWithMaxRetries));
        return factory;
    }

    public ConcurrentKafkaListenerContainerFactory<String, ?> createListenerFactory(KafkaOperations<String, Object> kafkaOperations,
                                                                                    Consumer consumer,
                                                                                    ConsumerFactory<String, ?> consumerFactory,
                                                                                    ApplicationContext applicationContext) {
        ConcurrentKafkaListenerContainerFactory<String, ?> singleKafkaListenerContainerFactory = createKafkaListenerContainerFactory(kafkaOperations, consumerFactory, consumer);
        ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        beanFactory.registerSingleton(consumer.getFactoryBeanName(), singleKafkaListenerContainerFactory);
        return singleKafkaListenerContainerFactory;
    }

    public Class<?> getDataClass(Consumer consumer) {
        try {
            return Class.forName(consumer.getDataClass());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}