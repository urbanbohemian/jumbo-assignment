package com.trendyol.international.commission.invoice.api.model.exception;

import lombok.Builder;

public class TrendyolDBException extends RuntimeException {
    @Builder
    public TrendyolDBException(Throwable cause) {
        super("TrendyolDBException", cause);
    }
}
