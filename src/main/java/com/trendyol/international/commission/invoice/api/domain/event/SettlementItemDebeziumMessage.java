package com.trendyol.international.commission.invoice.api.domain.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SettlementItemDebeziumMessage {
    private SettlementItemMessage before;
    private SettlementItemMessage after;
    private String op;
}
