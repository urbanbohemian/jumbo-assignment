package com.trendyol.international.commission.invoice.api.controller;


import com.trendyol.international.commission.invoice.api.model.document.PDFDocument;
import com.trendyol.international.commission.invoice.api.model.dto.CommissionInvoiceDTO;
import com.trendyol.international.commission.invoice.api.service.POCService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;

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
        CommissionInvoiceDTO commissionInvoiceDTO = new CommissionInvoiceDTO();
        commissionInvoiceDTO.setCreatedDate(new Date());
        commissionInvoiceDTO.setSerialNumber("BV1");
        commissionInvoiceDTO.setVatIdentificationNumber("NV1234567890");
        commissionInvoiceDTO.setFullName("Seller Finance");
        commissionInvoiceDTO.setAddress("Zuidplein 116 Tower H, F");
        commissionInvoiceDTO.setCommissionAmount(BigDecimal.valueOf(21.0));
        commissionInvoiceDTO.setGrossAmount(BigDecimal.valueOf(100.0));
        commissionInvoiceDTO.setNetAmount(BigDecimal.valueOf(79.0));

        PDFDocument pdfDocument = new PDFDocument();
        pdfDocument.setContent(new byte[]{1,2,3});

        when(pocService.createPDF(commissionInvoiceDTO)).thenReturn(pdfDocument);

        //when
        ResponseEntity<Resource> pdf = pocController.createPDF(commissionInvoiceDTO);

        //then
        assertThat(pdf).isNotNull();
    }
}
