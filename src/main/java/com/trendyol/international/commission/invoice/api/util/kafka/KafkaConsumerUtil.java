package com.trendyol.international.commission.invoice.api.util.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendyol.international.commission.invoice.api.config.kafka.KafkaConsumerInterceptor;
import com.trendyol.international.commission.invoice.api.config.kafka.KafkaProducerConsumerProps;
import com.trendyol.international.commission.invoice.api.service.failoverHandler.FailoverHandler;
import com.trendyol.international.commission.invoice.api.util.JsonSupport;
import com.trendyol.international.commission.invoice.api.util.SpringContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;
import org.springframework.util.backoff.FixedBackOff;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumerUtil implements JsonSupport {
    private final KafkaProducerConsumerProps kafkaProducerConsumerProps;
    private final ObjectMapper objectMapper;

    public <T> ConsumerFactory<String, T> createConsumerFactory(Consumer consumer, TypeReference<T> typeReference) {
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);

        var objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ErrorHandlingDeserializer<String> keyDeserializer = new ErrorHandlingDeserializer<>(new StringDeserializer());
        var messageJsonDeserializer = new JsonDeserializer<>(typeReference, objectMapper);
        messageJsonDeserializer.setTypeMapper(typeMapper);
        var valueDeserializer = new ErrorHandlingDeserializer<>(messageJsonDeserializer);

        // Add kafka consumer correlationid interceptor
        Map<String, Object> consumerProps = consumer.getProps();
        consumerProps.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, KafkaConsumerInterceptor.class.getName());
        return new DefaultKafkaConsumerFactory<>(consumerProps, keyDeserializer, valueDeserializer);
    }

    public <T> ConsumerFactory<String, T> createConsumerFactory(Consumer consumer, Class<T> classT) {
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);

        var objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ErrorHandlingDeserializer<String> keyDeserializer = new ErrorHandlingDeserializer<>(new StringDeserializer());
        var messageJsonDeserializer = new JsonDeserializer<>(classT, objectMapper);
        messageJsonDeserializer.setTypeMapper(typeMapper);
        var valueDeserializer = new ErrorHandlingDeserializer<>(messageJsonDeserializer);

        // Add kafka consumer correlationid interceptor
        Map<String, Object> consumerProps = consumer.getProps();
        consumerProps.putAll(kafkaProducerConsumerProps.getStretch());
        consumerProps.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, KafkaConsumerInterceptor.class.getName());

        return new DefaultKafkaConsumerFactory<>(consumerProps, keyDeserializer, valueDeserializer);
    }

    public void failoverProcessKafka(KafkaOperations<String, Object> kafkaOperations, Consumer consumer, ConsumerRecord consumerRecord) {
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
            failoverProcessKafka(kafkaOperations, consumer, record);
        }
    }

    //TODO: Making all utils as a library
    public <T> ConcurrentKafkaListenerContainerFactory<String, T> createSingleKafkaListenerContainerFactory(
            KafkaOperations<String, Object> kafkaOperations,
            ConsumerFactory<String, T> consumerFactory,
            Consumer consumer
    ) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setMissingTopicsFatal(Optional.ofNullable(consumer.getMissingTopicAlertEnable()).orElse(false));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        factory.getContainerProperties().setSyncCommits(Optional.of(consumer.isSyncCommit()).orElse(true));
        factory.getContainerProperties().setSyncCommitTimeout(Duration.ofSeconds(Optional.of(consumer.getSyncCommitTimeoutSecond()).orElse(5)));
        factory.setConcurrency(Optional.of(consumer.getConcurrency()).orElse(1));
        factory.setBatchListener(false);
        factory.setAutoStartup(Optional.ofNullable(consumer.getAutoStartup()).orElse(true));
        //TODO: Exponential BackOff Time
        //    https://docs.spring.io/spring-kafka/reference/html/
        //    ExponentialBackOffWithMaxRetries bo = new ExponentialBackOffWithMaxRetries(6);
        //    bo.setInitialInterval(1_000L);
        //    bo.setMultiplier(2.0);
        //    bo.setMaxInterval(10_000L);
        factory.setCommonErrorHandler(new DefaultErrorHandler((record, exception) ->
                handleFailover(kafkaOperations, consumer, record, exception),
                new FixedBackOff(Optional.of(consumer.getBackoffIntervalMillis()).orElse(50L), Optional.of(consumer.getRetryCount()).orElse(0)))
        );
        return factory;
    }
}
