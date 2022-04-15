package com.trendyol.international.commission.invoice.api.exception;

import lombok.Builder;

public class TrendyolRabbitException extends RuntimeException {
    @Builder
    public TrendyolRabbitException(Throwable cause) {
        super("TrendyolRabbitException", cause);
    }
}
