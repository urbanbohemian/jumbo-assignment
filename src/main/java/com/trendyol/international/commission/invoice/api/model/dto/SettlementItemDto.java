package com.trendyol.international.commission.invoice.api.model.dto;

import com.trendyol.international.commission.invoice.api.domain.event.SettlementItemMessage;
import com.trendyol.international.commission.invoice.api.model.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class SettlementItemDto {
    private Long settlementItemId;
    private Long sellerId;
    private BigDecimal price;
    private BigDecimal commission;
    private Date deliveryDate;
    private Date paymentDate;
    private Date createdDate;
    private TransactionType transactionType;

    public static SettlementItemDto fromDebeziumMessage(SettlementItemMessage settlementItemMessage) {
        return SettlementItemDto
                .builder()
                .settlementItemId(settlementItemMessage.getSettlementItemId())
                .sellerId(settlementItemMessage.getSellerId())
                .transactionType(TransactionType.from(settlementItemMessage.getType()))
                .price(settlementItemMessage.getPrice())
                .commission(settlementItemMessage.getTotalCommission())
                .deliveryDate(settlementItemMessage.getDeliveryDate())
                .paymentDate(settlementItemMessage.getPaymentDate())
                .createdDate(settlementItemMessage.getCreatedDate())
                .build();
    }
}
