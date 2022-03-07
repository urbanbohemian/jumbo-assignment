package com.trendyol.international.commission.invoice.api.model.dto;

import com.trendyol.international.commission.invoice.api.domain.event.SettlementItemMessage;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class SettlementItemDto {
    private Long sellerId;
    private BigDecimal price;
    private BigDecimal commission;
    private Date deliveryDate;
    private Date paymentDate;
    private Date createdDate;

    public static SettlementItemDto fromDebeziumMessage(SettlementItemMessage settlementItemMessage) {
        return SettlementItemDto
                .builder()
                .sellerId(settlementItemMessage.getSellerId())
                .price(settlementItemMessage.getPrice())
                .commission(settlementItemMessage.getTotalCommision())
                .deliveryDate(settlementItemMessage.getDeliveryDate())
                .paymentDate(settlementItemMessage.getPaymentDate())
                .createdDate(settlementItemMessage.getCreatedDate())
                .build();
    }
}
