package com.trendyol.international.commission.invoice.api.controller;

import com.trendyol.international.commission.invoice.api.model.request.CommissionInvoiceCreateRequest;
import com.trendyol.international.commission.invoice.api.service.CommissionInvoiceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CommissionInvoiceControllerTest {

    @InjectMocks
    public CommissionInvoiceController commissionInvoiceController;

    @Mock
    private CommissionInvoiceService commissionInvoiceService;

    @Test
    public void it_should_call_create() {
        //when
        commissionInvoiceController.create(new CommissionInvoiceCreateRequest());
        //then
        verify(commissionInvoiceService).create(any());
    }

    @Test
    public void it_should_call_generate_serial_number() {
        //when
        commissionInvoiceController.generateSerialNumber();
        //then
        verify(commissionInvoiceService).generateSerialNumber();
    }
}
