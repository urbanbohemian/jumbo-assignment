package com.trendyol.international.commission.invoice.api.exception;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class BaseTrendyolException extends RuntimeException {

    private final String key;
    private final String[] args;

    public BaseTrendyolException(String key) {
        this.key = key;
        this.args = ArrayUtils.EMPTY_STRING_ARRAY;
    }

    public BaseTrendyolException(String key, String... args) {
        this.key = key;
        this.args = args;
    }

    public String getKey() {
        return this.key;
    }

    public String[] getArgs() {
        return args;
    }

    @Override
    public String getMessage() {
        return "An exception occurred " + key + " " + StringUtils.join(args);
    }

}
