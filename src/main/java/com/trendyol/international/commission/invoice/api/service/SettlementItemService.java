package com.trendyol.international.commission.invoice.api.service;

import com.trendyol.international.commission.invoice.api.domain.SettlementItem;
import com.trendyol.international.commission.invoice.api.model.dto.SettlementItemDto;
import com.trendyol.international.commission.invoice.api.model.enums.TransactionType;
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
public class SettlementItemService implements FilterExtension {

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
                .isPresent();
    }

    @Override
    public void create(SettlementItemDto settlementItemDto) {
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
    public void update(SettlementItemDto settlementItemDto) {
        Integer affectedRows = settlementItemRepository.updateDeliveryDateAndPaymentDate(settlementItemDto.getDeliveryDate(), settlementItemDto.getPaymentDate(), settlementItemDto.getId());
        if (0 == affectedRows) {
            create(settlementItemDto);
        }
    }

    @Override
    public void handleError(SettlementItemDto model) {
        log.error("SettlementItem validation failed: {}", model);
    }

}
