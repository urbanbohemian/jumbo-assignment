package com.trendyol.international.commission.invoice.api.model.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class SettlementItemDto {
    private Long id;
    private Date createdDate;
    private Long sellerId;
    private Integer transactionType;
    private Date deliveryDate;
    private Date paymentDate;
    private BigDecimal commission;
    private Long storeFrontId;
    private String currency;
}
