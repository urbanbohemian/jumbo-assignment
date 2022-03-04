package com.trendyol.international.commission.invoice.api.domain.event;

import lombok.Data;

@Data
public class SettlementItemDebeziumMessage {
    private SettlementItemMessage before;
    private SettlementItemMessage after;
    private String op;

}
