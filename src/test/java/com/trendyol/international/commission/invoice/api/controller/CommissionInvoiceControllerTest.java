package com.trendyol.international.commission.invoice.api.controller;

import com.trendyol.international.commission.invoice.api.kafka.failover.KafkaConsumerExceptionService;
import com.trendyol.international.commission.invoice.api.service.CommissionInvoiceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CommissionInvoiceControllerTest {

    @InjectMocks
    public CommissionInvoiceController commissionInvoiceController;

    @Mock
    private CommissionInvoiceService commissionInvoiceService;

    @Mock
    private KafkaConsumerExceptionService kafkaConsumerExceptionService;

    @Test
    public void it_should_call_create() {
        //when
        commissionInvoiceController.create();
        //then
        verify(commissionInvoiceService).create();
    }

    @Test
    public void it_should_call_generate_serial_number() {
        //when
        commissionInvoiceController.generateSerialNumber();
        //then
        verify(commissionInvoiceService).generateSerialNumber();
    }

    @Test
    public void it_should_call_generate_pdf() {
        //when
        commissionInvoiceController.generatePdf();
        //then
        verify(commissionInvoiceService).generatePdf();
    }

    @Test
    public void it_should_call_envelope() {
        //when
        commissionInvoiceController.envelope();
        //then
        verify(commissionInvoiceService).envelope();
    }

    @Test
    public void it_should_call_shovel_exceptions() throws ClassNotFoundException {
        //when
        commissionInvoiceController.shovelExceptions();
        //then
        verify(kafkaConsumerExceptionService).shovelExceptions();
    }
}
