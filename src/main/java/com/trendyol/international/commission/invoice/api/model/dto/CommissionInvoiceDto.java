package com.trendyol.international.commission.invoice.api.model.dto;

import com.trendyol.international.commission.invoice.api.domain.SettlementItem;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class CommissionInvoiceDto {
    private Long sellerId;
    private Date startDate;
    private Date endDate;
    private List<SettlementItem> settlementItems;
}
