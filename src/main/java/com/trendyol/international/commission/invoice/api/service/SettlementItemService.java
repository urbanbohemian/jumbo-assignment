package com.trendyol.international.commission.invoice.api.service;

import com.trendyol.international.commission.invoice.api.domain.SettlementItem;
import com.trendyol.international.commission.invoice.api.repository.SettlementItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SettlementItemService {

    private final SettlementItemRepository settlementItemRepository;

    public SettlementItem saveSettlementItem(SettlementItem settlementItem) {
        return settlementItemRepository.save(settlementItem);
    }
}
