package com.trendyol.international.commission.invoice.api.service.failoverHandler;

import com.trendyol.international.commission.invoice.api.util.JsonSupport;
import com.trendyol.international.commission.invoice.api.util.kafka.Consumer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;

@RequiredArgsConstructor
public abstract class FailoverHandler implements JsonSupport {
    public abstract String getHashId(ConsumerRecord consumerRecord);

    public abstract void logException(Exception e);

    public abstract void persistException(Consumer consumer, ConsumerRecord consumerRecord, Exception exception, String hashId);

    public void handle(Consumer consumer, ConsumerRecord record, Exception exception) {
        String hashId = getHashId(record);
        persistException(consumer, record, exception, hashId);
        logException(exception);
    }
}