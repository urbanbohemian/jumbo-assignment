package com.trendyol.international.commission.invoice.api.exception;

public class TrendyolException extends RuntimeException {

    public TrendyolException(String key) {
        super(key);
    }

    public TrendyolException(String key, Throwable cause) {
        super(key, cause);
    }

}
