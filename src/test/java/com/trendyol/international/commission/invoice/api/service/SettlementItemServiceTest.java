package com.trendyol.international.commission.invoice.api.service;

import com.trendyol.international.commission.invoice.api.model.dto.SettlementItemDto;
import com.trendyol.international.commission.invoice.api.repository.SettlementItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Date;

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
        SettlementItemDto settlementItemDto = SettlementItemDto
                .builder()
                .sellerId(1L)
                .commission(BigDecimal.TEN)
                .createdDate(Date.valueOf("2022-03-07"))
                .price(BigDecimal.TEN)
                .build();

        //when
        settlementItemService.process(settlementItemDto);

        //then
        verify(settlementItemRepository).save(any());
    }

    @Test
    public void it_should_not_save_settlement_item_when_seller_id_is_null() {
        //given
        SettlementItemDto settlementItemDto = SettlementItemDto.builder().sellerId(null).build();
        //when
        settlementItemService.process(settlementItemDto);
        //then
        verifyNoInteractions(settlementItemRepository);
    }

    @Test
    public void it_should_not_save_settlement_item_when_price_is_null() {
        //given
        SettlementItemDto settlementItemDto = SettlementItemDto.builder().price(null).build();
        //when
        settlementItemService.process(settlementItemDto);
        //then
        verifyNoInteractions(settlementItemRepository);
    }

    @Test
    public void it_should_not_save_settlement_item_when_commission_is_null() {
        //given
        SettlementItemDto settlementItemDto = SettlementItemDto.builder().commission(null).build();
        //when
        settlementItemService.process(settlementItemDto);
        //then
        verifyNoInteractions(settlementItemRepository);
    }

    @Test
    public void it_should_not_save_settlement_item_when_created_date_is_null() {
        //given
        SettlementItemDto settlementItemDto = SettlementItemDto.builder().createdDate(null).build();
        //when
        settlementItemService.process(settlementItemDto);
        //then
        verifyNoInteractions(settlementItemRepository);
    }

}
