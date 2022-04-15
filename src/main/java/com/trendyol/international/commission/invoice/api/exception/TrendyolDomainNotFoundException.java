package com.trendyol.international.commission.invoice.api.exception;

import java.util.function.Supplier;

public final class TrendyolDomainNotFoundException extends BaseTrendyolException implements Supplier<TrendyolDomainNotFoundException> {

    public TrendyolDomainNotFoundException(String key) {
        super(key);
    }

    public TrendyolDomainNotFoundException(String key, String... args) {
        super(key, args);
    }

    @Override
    public TrendyolDomainNotFoundException get() {
        return new TrendyolDomainNotFoundException(getKey(), getArgs());
    }
}
