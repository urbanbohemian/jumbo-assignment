package com.trendyol.international.commission.invoice.api.service;

import com.trendyol.international.commission.invoice.api.domain.SettlementItem;
import com.trendyol.international.commission.invoice.api.model.dto.SettlementItemDto;
import com.trendyol.international.commission.invoice.api.repository.SettlementItemRepository;
import com.trendyol.international.commission.invoice.api.util.FilterExtension;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class SettlementItemService implements FilterExtension<SettlementItemDto> {

    private final SettlementItemRepository settlementItemRepository;

    @Override
    public boolean applyFilter(SettlementItemDto model) {
        return Optional.ofNullable(model)
                .filter(m -> Objects.nonNull(m.getSellerId()))
                .filter(m -> Objects.nonNull(m.getCommission()))
                .filter(m -> Objects.nonNull(m.getCreatedDate()))
                .isPresent();
    }

    @Override
    public void execute(SettlementItemDto settlementItemDto) {
        SettlementItem settlementItem = SettlementItem
                .builder()
                .sellerId(settlementItemDto.getSellerId())
                .transactionType(settlementItemDto.getTransactionType())
                .commissionAmount(settlementItemDto.getCommission())
                .deliveryDate(settlementItemDto.getDeliveryDate())
                .paymentDate(settlementItemDto.getPaymentDate())
                .itemCreationDate(settlementItemDto.getCreatedDate())
                .build();
        settlementItemRepository.save(settlementItem);
    }

    @Override
    public void handleError(SettlementItemDto model) {
        log.error("SettlementItem validation failed: {}", model);
    }


}
