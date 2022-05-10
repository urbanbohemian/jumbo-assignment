package com.trendyol.international.commission.invoice.api.model.exception;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ExceptionModel {
    private String exception;
    private String message;
}
