package com.trendyol.international.commission.invoice.api.service.failoverHandler;

import com.trendyol.international.commission.invoice.api.util.kafka.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface FailoverHandler {
    void handle(Consumer consumer, ConsumerRecord record, Exception exception);
}