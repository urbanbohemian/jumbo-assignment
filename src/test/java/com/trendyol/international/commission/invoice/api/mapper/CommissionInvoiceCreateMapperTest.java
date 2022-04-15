package com.trendyol.international.commission.invoice.api.mapper;

import com.trendyol.international.commission.invoice.api.domain.event.CommissionInvoiceCreateEvent;
import com.trendyol.international.commission.invoice.api.model.dto.CommissionInvoiceCreateDto;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommissionInvoiceCreateMapperTest {

    @Test
    public void it_should_call_create() {
        //given
        Date currentDate = new Date();

        CommissionInvoiceCreateEvent commissionInvoiceCreateEvent = CommissionInvoiceCreateEvent.builder()
                .sellerId(1L)
                .country("NL")
                .currency("EUR")
                .automaticInvoiceStartDate(currentDate)
                .endDate(currentDate)
                .build();

        //when
        CommissionInvoiceCreateDto commissionInvoiceCreateDto = CommissionInvoiceCreateMapper.INSTANCE.commissionInvoiceCreateDto(commissionInvoiceCreateEvent);

        //then
        assertEquals(commissionInvoiceCreateDto.getSellerId(), commissionInvoiceCreateEvent.getSellerId());
        assertEquals(commissionInvoiceCreateDto.getCountry(), commissionInvoiceCreateEvent.getCountry());
        assertEquals(commissionInvoiceCreateDto.getCurrency(), commissionInvoiceCreateEvent.getCurrency());
        assertEquals(commissionInvoiceCreateDto.getAutomaticInvoiceStartDate(), commissionInvoiceCreateEvent.getAutomaticInvoiceStartDate());
        assertEquals(commissionInvoiceCreateDto.getEndDate(), commissionInvoiceCreateEvent.getEndDate());
    }
}