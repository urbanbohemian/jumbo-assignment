package com.trendyol.international.commission.invoice.api.controller;


import com.trendyol.international.commission.invoice.api.model.document.PDFDocument;
import com.trendyol.international.commission.invoice.api.model.dto.CommissionInvoice;
import com.trendyol.international.commission.invoice.api.model.dto.InvoiceLineItem;
import com.trendyol.international.commission.invoice.api.model.dto.InvoiceLineItemDtoBuilder;
import com.trendyol.international.commission.invoice.api.service.POCService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class POCControllerTest {

    @InjectMocks
    private POCController pocController;

    @Mock
    private POCService pocService;

    @Test
    public void it_should_get_valid_model_for_pdf() {

        //given
        CommissionInvoice commissionInvoiceDTO = new CommissionInvoice();
        commissionInvoiceDTO.setCreatedDate(new Date());
        commissionInvoiceDTO.setSerialNumber("BV1");
        commissionInvoiceDTO.setVatIdentificationNumber("NV1234567890");
        commissionInvoiceDTO.setFullName("Seller Finance");
        commissionInvoiceDTO.setAddress("Zuidplein 116 Tower H, F");
        List<InvoiceLineItem> invoiceLineItems = new ArrayList<>();
        invoiceLineItems.add(InvoiceLineItemDtoBuilder.anInvoiceLineItemDto()
                .Title("Commission Amount")
                .Amount(BigDecimal.valueOf(29.95))
                .Currency("€")
                .build());

        invoiceLineItems.add(InvoiceLineItemDtoBuilder.anInvoiceLineItemDto()
                .Title("Net Amount")
                .Amount(BigDecimal.valueOf(21.95))
                .Currency("$")
                .build());

        commissionInvoiceDTO.setLineItems(invoiceLineItems);


        PDFDocument pdfDocument = new PDFDocument();
        pdfDocument.setContent(new byte[]{1,2,3});

        when(pocService.createPDF(commissionInvoiceDTO)).thenReturn(pdfDocument);

        //when
        ResponseEntity<Resource> pdf = pocController.createPDF(commissionInvoiceDTO);

        //then
        assertThat(pdf).isNotNull();
    }
}
