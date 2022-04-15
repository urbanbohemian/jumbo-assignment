package com.trendyol.international.commission.invoice.api.domain.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SettlementItemDebeziumEvent {
    private SettlementItemEvent before;
    private SettlementItemEvent after;
    private String op;
}
