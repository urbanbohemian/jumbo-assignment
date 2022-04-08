package com.trendyol.international.commission.invoice.api.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendyol.international.commission.invoice.api.config.kafka.KafkaConsumerInterceptor;
import com.trendyol.international.commission.invoice.api.config.kafka.KafkaProducerConsumerProps;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.listener.adapter.RetryingMessageListenerAdapter;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.backoff.FixedBackOff;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@Component
public class KafkaConsumerUtil {

    private final KafkaProducerConsumerProps kafkaProducerConsumerProps;

    public KafkaConsumerUtil(KafkaProducerConsumerProps kafkaProducerConsumerProps) {
        this.kafkaProducerConsumerProps = kafkaProducerConsumerProps;
    }

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

    public RetryTemplate createRetryTemplate(Consumer consumer) {
        RetryTemplate retryTemplate = new RetryTemplate();

        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(consumer.getBackoffIntervalMillis());
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(consumer.getRetryCount() + 1);
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;
    }

    public <T> ConcurrentKafkaListenerContainerFactory<String, T> createSingleKafkaListenerContainerFactory(
            KafkaOperations<String, Object> kafkaOperations,
            ConsumerFactory<String, T> consumerFactory,
            Consumer consumer,
            RetryTemplate retryTemplate
    ) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setMissingTopicsFatal(Optional.ofNullable(consumer.getMissingTopicAlertEnable()).orElse(true));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        factory.getContainerProperties().setSyncCommits(consumer.isSyncCommit());
        factory.getContainerProperties().setSyncCommitTimeout(Duration.ofSeconds(consumer.getSyncCommitTimeoutSecond()));
        factory.setConcurrency(consumer.getConcurrency());

        //TODO: Daha sonra retry-count'u max-attempt olarak refactor edebilirsin.
        if(consumer.getRetryCount() >= 0) {
            factory.setRetryTemplate(retryTemplate);
            Optional.ofNullable(consumer.getErrorTopic())
                    .ifPresent(errorTopic -> {
                        ErrorHandler errorHandler = new SeekToCurrentErrorHandler(new FixedBackOff(consumer.getBackoffIntervalMillis(),
                                consumer.getRetryCount() + 1));
                        factory.setRecoveryCallback(context -> {
                            ConsumerRecord record = (ConsumerRecord) context.getAttribute(RetryingMessageListenerAdapter.CONTEXT_RECORD);
                            kafkaOperations.send(consumer.getErrorTopic(), record.key().toString(), record.value());
                            return Optional.empty();
                        });
                        factory.setErrorHandler(errorHandler);
                    });
        }
        factory.setBatchListener(false);

        return factory;
    }
}
