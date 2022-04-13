package com.trendyol.international.commission.invoice.api.mapper;

import com.trendyol.international.commission.invoice.api.domain.event.CommissionInvoiceCreateMessage;
import com.trendyol.international.commission.invoice.api.model.dto.CommissionInvoiceCreateDto;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommissionInvoiceCreateMapperTest {

    @Test
    public void it_should_call_create() {
        //given
        Date currentDate = new Date();

        CommissionInvoiceCreateMessage commissionInvoiceCreateMessage = CommissionInvoiceCreateMessage.builder()
                .sellerId(1L)
                .country("NL")
                .currency("EUR")
                .automaticInvoiceStartDate(currentDate)
                .endDate(currentDate)
                .build();

        //when
        CommissionInvoiceCreateDto commissionInvoiceCreateDto = CommissionInvoiceCreateMapper.INSTANCE.commissionInvoiceCreateDto(commissionInvoiceCreateMessage);

        //then
        assertEquals(commissionInvoiceCreateDto.getSellerId(), commissionInvoiceCreateMessage.getSellerId());
        assertEquals(commissionInvoiceCreateDto.getCountry(), commissionInvoiceCreateMessage.getCountry());
        assertEquals(commissionInvoiceCreateDto.getCurrency(), commissionInvoiceCreateMessage.getCurrency());
        assertEquals(commissionInvoiceCreateDto.getAutomaticInvoiceStartDate(), commissionInvoiceCreateMessage.getAutomaticInvoiceStartDate());
        assertEquals(commissionInvoiceCreateDto.getEndDate(), commissionInvoiceCreateMessage.getEndDate());
    }
}