package com.trendyol.international.commission.invoice.api.exception;

public class TrendyolSystemException extends RuntimeException {

    private static final long serialVersionUID = -4762399267164231067L;

    public TrendyolSystemException(String message) {
        super(message);
    }

    public TrendyolSystemException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
