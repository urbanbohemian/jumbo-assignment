package com.trendyol.international.commission.invoice.api.service;

import com.trendyol.international.commission.invoice.api.domain.SettlementItem;
import com.trendyol.international.commission.invoice.api.model.dto.SettlementItemDto;
import com.trendyol.international.commission.invoice.api.model.enums.TransactionType;
import com.trendyol.international.commission.invoice.api.repository.SettlementItemRepository;
import com.trendyol.international.commission.invoice.api.util.FilterExtension;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
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
                .filter(m -> Objects.nonNull(m.getId()))
                .filter(m -> Objects.nonNull(m.getCreatedDate()))
                .filter(m -> Objects.nonNull(m.getSellerId()))
                .filter(m -> Objects.nonNull(m.getTransactionType()))
                .filter(m -> Objects.nonNull(m.getCommission()))
                .filter(m -> Objects.nonNull(m.getStoreFrontId()))
                .filter(m -> Objects.nonNull(m.getCurrency()))
                .filter(m -> Objects.nonNull(m.getPaymentDate()))
                .filter(m -> List.of(TransactionType.SALE.getId(), TransactionType.RETURN.getId()).contains(m.getTransactionType()))
                .isPresent();
    }

    @Override
    public void execute(SettlementItemDto settlementItemDto) {
        SettlementItem settlementItem = SettlementItem.builder()
                .id(settlementItemDto.getId())
                .itemCreationDate(settlementItemDto.getCreatedDate())
                .sellerId(settlementItemDto.getSellerId())
                .transactionType(TransactionType.from(settlementItemDto.getTransactionType()))
                .commissionAmount(settlementItemDto.getCommission())
                .deliveryDate(settlementItemDto.getDeliveryDate())
                .paymentDate(settlementItemDto.getPaymentDate())
                .storeFrontId(settlementItemDto.getStoreFrontId())
                .currency(settlementItemDto.getCurrency())
                .build();
        settlementItemRepository.save(settlementItem);
    }

    @Override
    public void handleError(SettlementItemDto model) {
        log.error("SettlementItem validation failed: {}", model);
    }

}
