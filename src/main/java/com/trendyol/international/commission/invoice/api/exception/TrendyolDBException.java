package com.trendyol.international.commission.invoice.api.exception;

import lombok.Builder;

public class TrendyolDBException extends RuntimeException {
    @Builder
    public TrendyolDBException(Throwable cause) {
        super("TrendyolDBException", cause);
    }
}
