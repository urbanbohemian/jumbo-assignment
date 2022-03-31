package com.trendyol.international.commission.invoice.api.domain.event;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@Data
public class InvoiceLine {
    private String description;
    private Integer quantity;
    private String unit;
    private BigDecimal unitPrice;
    private BigDecimal vatRate;
    private BigDecimal amount;
    private String referenceId;
    private String invoiceNumber;
    private Date invoiceDate;
    private String vatStatusType;
}
