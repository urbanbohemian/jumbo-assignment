package com.trendyol.international.commission.invoice.api.model.exception;

import lombok.Builder;

public class TrendyolKafkaException extends RuntimeException {
    @Builder
    public TrendyolKafkaException(Throwable cause) {
        super("TrendyolKafkaException", cause);
    }
}
