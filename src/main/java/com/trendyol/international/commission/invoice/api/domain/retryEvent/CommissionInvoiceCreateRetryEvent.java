package com.trendyol.international.commission.invoice.api.domain.retryEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CommissionInvoiceCreateRetryEvent {
    private Long sellerId;
    private String country;
    private String currency;
    private Date automaticInvoiceStartDate;
    private Date endDate;
}
