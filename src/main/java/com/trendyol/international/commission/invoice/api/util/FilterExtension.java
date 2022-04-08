package com.trendyol.international.commission.invoice.api.util;

import com.trendyol.international.commission.invoice.api.domain.event.SettlementItemDebeziumMessage;
import com.trendyol.international.commission.invoice.api.mapper.SettlementItemMapper;
import com.trendyol.international.commission.invoice.api.model.dto.SettlementItemDto;

import java.util.Optional;

public interface FilterExtension {

    default boolean applyFilter(SettlementItemDto model) {
        return true;
    }

    default void process(SettlementItemDebeziumMessage model) {
        SettlementItemDto dto = SettlementItemMapper.INSTANCE.settlementItemDto(model.getAfter());
        Optional.ofNullable(dto).filter(this::applyFilter).ifPresentOrElse(m -> {
            if ("c".equals(model.getOp())) {
                create(dto);
            } else if ("u".equals(model.getOp())) {
                update(dto);
            }
        }, () -> handleError(dto));
    }

    void create(SettlementItemDto model);

    void update(SettlementItemDto model);

    void handleError(SettlementItemDto model);
}
