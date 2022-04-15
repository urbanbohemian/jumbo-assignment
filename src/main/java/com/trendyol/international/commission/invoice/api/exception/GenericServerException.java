package com.trendyol.international.commission.invoice.api.exception;

import java.util.function.Supplier;

public class GenericServerException extends BaseTrendyolException implements Supplier<GenericServerException> {

    public GenericServerException(String key) {
        super(key);
    }

    public GenericServerException(String key, String... args) {
        super(key, args);
    }

    @Override
    public GenericServerException get() {
        return new GenericServerException(getKey(), getArgs());
    }
}
