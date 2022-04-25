package com.trendyol.international.commission.invoice.api.kafka.interceptor;

import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.Header;
import org.slf4j.MDC;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

import static com.trendyol.international.commission.invoice.api.util.AuditionConstants.X_CORRELATION_ID_CAMEL_CASE;

public class KafkaConsumerInterceptor implements ConsumerInterceptor<String, String> {

    @Override
    public ConsumerRecords<String, String> onConsume(ConsumerRecords<String, String> consumerRecords) {
        ConsumerRecord<String, String> record = consumerRecords.iterator().next();

        setCorrelationId(record.headers().headers(X_CORRELATION_ID_CAMEL_CASE));

        return consumerRecords;
    }

    @Override
    public void onCommit(Map<TopicPartition, OffsetAndMetadata> map) {
        // Do nothing because not necessary
    }

    @Override
    public void close() {
        // Do nothing because not necessary
    }

    @Override
    public void configure(Map<String, ?> map) {
        // Do nothing because not necessary
    }

    private void setCorrelationId(Iterable<Header> correlationIdHeader) {
        String correlationId = UUID.randomUUID().toString();
        if (correlationIdHeader.iterator().hasNext()) {
            Header header = correlationIdHeader.iterator().next();
            correlationId = new String(header.value(), StandardCharsets.UTF_8);
        }

        MDC.put(X_CORRELATION_ID_CAMEL_CASE, correlationId);
    }

}
