package com.trendyol.international.commission.invoice.api.model.exception;

import java.util.function.Supplier;

public class TooManyRequestsException extends BaseTrendyolException implements Supplier<TooManyRequestsException> {

    public TooManyRequestsException(String key) {
        super(key);
    }

    public TooManyRequestsException(String key, String... args) {
        super(key, args);
    }

    @Override
    public TooManyRequestsException get() {
        return new TooManyRequestsException(getKey(), getArgs());
    }
}
