package com.trendyol.international.commission.invoice.api.util.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface FailoverHandler {
    void handle(ConsumerRecord consumerRecord, Exception exception, String dataClass);
}
