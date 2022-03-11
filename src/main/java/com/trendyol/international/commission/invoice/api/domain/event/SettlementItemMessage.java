package com.trendyol.international.commission.invoice.api.domain.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SettlementItemMessage {
    private Long settlementItemId;
    private Long sellerId;
    private String type;
    private Date deliveryDate;
    private Date paymentDate;
    private BigDecimal totalCommission;
    private Long storeFrontId;
    private String currency;
    private Date createdDate;
}