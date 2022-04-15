package com.trendyol.international.commission.invoice.api.exception;

import lombok.Builder;

public class TrendyolKafkaException extends RuntimeException {
    @Builder
    public TrendyolKafkaException(Throwable cause) {
        super("TrendyolKafkaException", cause);
    }
}
