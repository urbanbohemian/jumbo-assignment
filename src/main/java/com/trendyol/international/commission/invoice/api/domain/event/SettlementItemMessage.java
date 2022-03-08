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
    private Long oliId;
    private Long sellerId;
    private String type;
    private BigDecimal price;
    private BigDecimal commissionRate;
    private Date deliveryDate;
    private Date paymentDate;
    private BigDecimal couponPrice;
    private BigDecimal totalPrice;
    private BigDecimal totalCommission;
    private BigDecimal totalSellerRevenue;
    private Long storeFrontId;
    private String currency;
    private Date createdDate;
}
