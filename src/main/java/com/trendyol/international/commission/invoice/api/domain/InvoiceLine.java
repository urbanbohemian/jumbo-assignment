package com.trendyol.international.commission.invoice.api.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class InvoiceLine {
    private String description;
    private Integer quantity;
    private String unit;
    private BigDecimal unitPrice;
    private BigDecimal vatRate;
    private BigDecimal amount;
}
