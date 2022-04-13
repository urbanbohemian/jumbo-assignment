package com.trendyol.international.commission.invoice.api.service;

import com.trendyol.international.commission.invoice.api.model.dto.SettlementItemDto;
import com.trendyol.international.commission.invoice.api.model.enums.TransactionType;
import com.trendyol.international.commission.invoice.api.repository.SettlementItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public class SettlementItemServiceTest {

    @InjectMocks
    private SettlementItemService settlementItemService;

    @Mock
    private SettlementItemRepository settlementItemRepository;

    @Test
    public void it_should_save_settlement_item() {
        //given
        SettlementItemDto settlementItemDto = SettlementItemDto.builder()
                .id(1L)
                .createdDate(Date.valueOf("2022-03-25"))
                .sellerId(1L)
                .transactionType(TransactionType.SALE.getId())
                .deliveryDate(Date.valueOf("2022-03-25"))
                .paymentDate(Date.valueOf("2022-03-25"))
                .commission(BigDecimal.TEN)
                .storeFrontId(1L)
                .currency("EU")
                .build();

        //when
        settlementItemService.process(settlementItemDto);

        //then
        verify(settlementItemRepository).save(any());
    }

    @Test
    public void it_should_not_save_settlement_item_when_dto_is_not_valid() {
        //given
        SettlementItemDto settlementItemDto = SettlementItemDto.builder().build();
        //when
        boolean filterResult = settlementItemService.applyFilter(settlementItemDto);
        //then
        verifyNoInteractions(settlementItemRepository);
        assertThat(filterResult).isFalse();
    }

    @Test
    public void it_should_not_save_settlement_item_when_transaction_type_is_cancel() {
        //given
        SettlementItemDto settlementItemDto = SettlementItemDto.builder()
                .id(1L)
                .createdDate(Date.valueOf("2022-03-25"))
                .sellerId(1L)
                .transactionType(TransactionType.CANCEL.getId())
                .deliveryDate(Date.valueOf("2022-03-25"))
                .paymentDate(Date.valueOf("2022-03-25"))
                .commission(BigDecimal.TEN)
                .storeFrontId(1L)
                .currency("EU")
                .build();

        //when
        boolean filterResult = settlementItemService.applyFilter(settlementItemDto);

        //then
        verifyNoInteractions(settlementItemRepository);
        assertThat(filterResult).isFalse();
    }

    @Test
    public void it_should_not_save_settlement_item_when_payment_date_is_null() {
        //given
        SettlementItemDto settlementItemDto = SettlementItemDto.builder()
                .id(1L)
                .createdDate(Date.valueOf("2022-03-25"))
                .sellerId(1L)
                .transactionType(TransactionType.SALE.getId())
                .deliveryDate(Date.valueOf("2022-03-25"))
                .paymentDate(null)
                .commission(BigDecimal.TEN)
                .storeFrontId(1L)
                .currency("EU")
                .build();

        //when
        boolean filterResult = settlementItemService.applyFilter(settlementItemDto);

        //then
        verifyNoInteractions(settlementItemRepository);
        assertThat(filterResult).isFalse();
    }
}
