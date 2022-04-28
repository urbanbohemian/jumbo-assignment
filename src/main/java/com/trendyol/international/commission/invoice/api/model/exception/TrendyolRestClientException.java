package com.trendyol.international.commission.invoice.api.model.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TrendyolRestClientException extends RuntimeException {
    private final HttpStatus status;
    private final String errorMessage;
    private final String errorKey;

    @Builder
    public TrendyolRestClientException(Throwable cause, HttpStatus status, String errorMessage, String errorKey) {
        super("TrendyolRestClientException", cause);
        this.status = status;
        this.errorMessage = errorMessage;
        this.errorKey = errorKey;
    }
}
