package com.trendyol.international.commission.invoice.api.domain.event;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@Data
public class DocumentCreateEvent {
    private Long sellerId;
    private String sellerName;
    private String addressLine;
    private String email;
    private String phone;
    private String invoiceNumber;
    private Date invoiceDate;
    private String taxIdentificationNumber;
    private String vatIdentificationNumber;
    private String registrationNumber;
    private String referenceId;
    private String vatStatusType;
    private BigDecimal netAmount;
    private BigDecimal vatAmount;
    private BigDecimal vatRate;
    private BigDecimal amount;
}
