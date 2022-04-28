package com.trendyol.international.commission.invoice.api.model.exception;

import java.util.function.Supplier;

public class TrendyolBusinessException extends BaseTrendyolException implements Supplier<TrendyolBusinessException> {

    public TrendyolBusinessException(String key) {
        super(key);
    }

    public TrendyolBusinessException(String key, String... args) {
        super(key, args);
    }

    @Override
    public TrendyolBusinessException get() {
        return new TrendyolBusinessException(getKey(), getArgs());
    }
}
