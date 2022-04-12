package com.trendyol.international.commission.invoice.api.domain.event;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class CommissionInvoiceCreateMessage {
    private Long sellerId;
    private String country;
    private String currency;
    private Date automaticInvoiceStartDate;
    private Date endDate;
}
