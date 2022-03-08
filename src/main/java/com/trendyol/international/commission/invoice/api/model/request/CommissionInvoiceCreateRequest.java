package com.trendyol.international.commission.invoice.api.model.request;

import lombok.Data;

import java.util.Date;

@Data
public class CommissionInvoiceCreateRequest {
    private Long sellerId;
    private Date jobExecutionDate;
    private String country;
    private String currency;
}
