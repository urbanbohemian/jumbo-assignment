package com.trendyol.international.commission.invoice.api.mapper;

import com.trendyol.international.commission.invoice.api.domain.event.SettlementItemEvent;
import com.trendyol.international.commission.invoice.api.model.dto.SettlementItemDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SettlementItemMapperTest {

    @Test
    public void it_should_call_create() {
        //given
        Date currentDate = new Date();

        SettlementItemEvent settlementItemEvent = new SettlementItemEvent();
        settlementItemEvent.setId(1L);
        settlementItemEvent.setCreatedDate(currentDate);
        settlementItemEvent.setSellerId(1L);
        settlementItemEvent.setTransactionTypeId(1);
        settlementItemEvent.setDeliveryDate(currentDate);
        settlementItemEvent.setPaymentDate(currentDate);
        settlementItemEvent.setTotalCommission(BigDecimal.ONE);
        settlementItemEvent.setStoreFrontId(1L);
        settlementItemEvent.setCurrency("EUR");

        //when
        SettlementItemDto settlementItemDto = SettlementItemMapper.INSTANCE.settlementItemDto(settlementItemEvent);

        //then
        assertEquals(settlementItemDto.getId(), settlementItemEvent.getId());
        assertEquals(settlementItemDto.getCreatedDate(), settlementItemEvent.getCreatedDate());
        assertEquals(settlementItemDto.getSellerId(), settlementItemEvent.getSellerId());
        assertEquals(settlementItemDto.getTransactionType(), settlementItemEvent.getTransactionTypeId());
        assertEquals(settlementItemDto.getDeliveryDate(), settlementItemEvent.getDeliveryDate());
        assertEquals(settlementItemDto.getPaymentDate(), settlementItemEvent.getPaymentDate());
        assertEquals(settlementItemDto.getCommission(), settlementItemEvent.getTotalCommission());
        assertEquals(settlementItemDto.getStoreFrontId(), settlementItemEvent.getStoreFrontId());
        assertEquals(settlementItemDto.getCurrency(), settlementItemEvent.getCurrency());
    }
}