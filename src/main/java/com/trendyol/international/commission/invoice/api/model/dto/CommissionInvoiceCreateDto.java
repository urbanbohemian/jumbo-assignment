package com.trendyol.international.commission.invoice.api.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CommissionInvoiceCreateDto {
    private Long sellerId;
    private String country;
    private String currency;
    private Date automaticInvoiceStartDate;
    private Date endDate;
}
