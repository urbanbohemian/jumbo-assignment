package com.trendyol.international.commission.invoice.api.mapper;

import com.trendyol.international.commission.invoice.api.domain.event.SettlementItemMessage;
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

        SettlementItemMessage settlementItemMessage = new SettlementItemMessage();
        settlementItemMessage.setId(1L);
        settlementItemMessage.setCreatedDate(currentDate);
        settlementItemMessage.setSellerId(1L);
        settlementItemMessage.setType("Sale");
        settlementItemMessage.setDeliveryDate(currentDate);
        settlementItemMessage.setPaymentDate(currentDate);
        settlementItemMessage.setTotalCommission(BigDecimal.ONE);
        settlementItemMessage.setStoreFrontId(1L);
        settlementItemMessage.setCurrency("EUR");

        //when
        SettlementItemDto settlementItemDto = SettlementItemMapper.INSTANCE.settlementItemDto(settlementItemMessage);

        //then
        assertEquals(settlementItemDto.getId(), settlementItemMessage.getId());
        assertEquals(settlementItemDto.getCreatedDate(), settlementItemMessage.getCreatedDate());
        assertEquals(settlementItemDto.getSellerId(), settlementItemMessage.getSellerId());
        assertEquals(settlementItemDto.getTransactionType().name(), settlementItemMessage.getType());
        assertEquals(settlementItemDto.getDeliveryDate(), settlementItemMessage.getDeliveryDate());
        assertEquals(settlementItemDto.getPaymentDate(), settlementItemMessage.getPaymentDate());
        assertEquals(settlementItemDto.getCommission(), settlementItemMessage.getTotalCommission());
        assertEquals(settlementItemDto.getStoreFrontId(), settlementItemMessage.getStoreFrontId());
        assertEquals(settlementItemDto.getCurrency(), settlementItemMessage.getCurrency());
    }
}