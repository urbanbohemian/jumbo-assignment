package com.trendyol.international.commission.invoice.api.mapper;

import com.trendyol.international.commission.invoice.api.model.dto.CommissionInvoiceCreateDto;
import com.trendyol.international.commission.invoice.api.model.request.CommissionInvoiceCreateRequest;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class CommissionInvoiceCreateMapperTest {

    @Test
    public void it_should_call_create() {
        //given
        Date currentDate = new Date();

        CommissionInvoiceCreateRequest commissionInvoiceCreateRequest = new CommissionInvoiceCreateRequest();
        commissionInvoiceCreateRequest.setSellerId(1L);
        commissionInvoiceCreateRequest.setJobExecutionDate(currentDate);
        commissionInvoiceCreateRequest.setCountry("NL");
        commissionInvoiceCreateRequest.setCurrency("EUR");

        //when
        CommissionInvoiceCreateDto commissionInvoiceCreateDto = CommissionInvoiceCreateMapper.INSTANCE.commissionInvoiceCreateDto(commissionInvoiceCreateRequest);

        //then
        assertEquals(commissionInvoiceCreateDto.getSellerId(), commissionInvoiceCreateRequest.getSellerId());
        assertEquals(commissionInvoiceCreateDto.getJobExecutionDate(), commissionInvoiceCreateRequest.getJobExecutionDate());
        assertEquals(commissionInvoiceCreateDto.getCountry(), commissionInvoiceCreateRequest.getCountry());
        assertEquals(commissionInvoiceCreateDto.getCurrency(), commissionInvoiceCreateRequest.getCurrency());
    }
}