package com.trendyol.international.commission.invoice.api.domain.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trendyol.international.commission.invoice.api.util.Hashing;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SettlementItemDebeziumEvent {
    private SettlementItemEvent before;
    private SettlementItemEvent after;
    private String op;

    @JsonIgnore
    public String getHashId() {
        return Hashing.md5("TODO!");
    }
}
