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
    private BigDecimal commission;
    private Date deliveryDate;
    private Date paymentDate;
    private Date createdDate;
    private TransactionType transactionType;
    private Long storeFrontId;

    public static SettlementItemDto fromDebeziumMessage(SettlementItemMessage settlementItemMessage) {
        return SettlementItemDto
                .builder()
                .settlementItemId(settlementItemMessage.getSettlementItemId())
                .sellerId(settlementItemMessage.getSellerId())
                .transactionType(TransactionType.from(settlementItemMessage.getType()))
                .commission(settlementItemMessage.getTotalCommission())
                .deliveryDate(settlementItemMessage.getDeliveryDate())
                .paymentDate(settlementItemMessage.getPaymentDate())
                .createdDate(settlementItemMessage.getCreatedDate())
                .storeFrontId(settlementItemMessage.getStoreFrontId())
                .build();
    }
}
