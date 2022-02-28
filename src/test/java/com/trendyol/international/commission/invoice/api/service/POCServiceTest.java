package com.trendyol.international.commission.invoice.api.service;

import com.trendyol.international.commission.invoice.api.model.document.PDFDocument;
import com.trendyol.international.commission.invoice.api.model.dto.CommissionInvoice;
import com.trendyol.international.commission.invoice.api.model.dto.InvoiceLineItem;
import com.trendyol.international.commission.invoice.api.model.dto.InvoiceLineItemDtoBuilder;
import org.assertj.core.internal.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class POCServiceTest {

    @InjectMocks
    @Spy
    POCService pocService;

    @Disabled
    @Test
    void it_should_create_pdf(){
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
        pdfDocument.setName("INTERNATIONAL_PDF");
        pdfDocument.setContent(new byte[]{1, 2, 3});


        when(pocService.getHtmlSource("classpath:invoice_old.html")).thenReturn("content");
        when(pocService.convertToPDFDocument("content")).thenReturn(pdfDocument);

        //then
        PDFDocument pdf = pocService.createPDF(commissionInvoiceDTO);

        assertThat(pdf).isNotNull();
        assertThat(pdf.getContent()).isEqualTo(pdfDocument.getContent());

    }

    @Disabled
    @Test
    void it_should_get_html_source(){
        //given
        File htmlFile = null;
        String htmlContent = "";
        try {
            htmlFile = ResourceUtils.getFile("classpath:invoice_old.html");
            htmlContent = new String(Files.readAllBytes(htmlFile.toPath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //when
        String actualHtmlSource = pocService.getHtmlSource("classpath:invoice_old.html");

        assertThat(actualHtmlSource).isNotBlank();
        assertThat(actualHtmlSource).isEqualTo(htmlContent);
    }

    @Disabled
    @Test
    void it_should_fill_html_source(){
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
        pdfDocument.setName("INTERNATIONAL_PDF");
        pdfDocument.setContent(new byte[]{1, 2, 3});

        File htmlFile = null;
        String htmlContent = "";
        try {
            htmlFile = ResourceUtils.getFile("classpath:invoice_old.html");
            htmlContent = new String(Files.readAllBytes(htmlFile.toPath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //when
        String filledHtml = pocService.pdfFiller(commissionInvoiceDTO,htmlContent);

        assertThat(filledHtml).isNotBlank();
        assertThat(filledHtml).contains("Zuidplein 116 Tower H, F");
        assertThat(filledHtml).contains("NV1234567890");
    }

    @Disabled
    @Test
    void it_should_convert_to_pdf_document(){
        //given
        String source = "Seller Finance";

        //when
        PDFDocument pdfDocument = pocService.convertToPDFDocument(source);

        assertThat(pdfDocument).isNotNull();
        assertThat(pdfDocument.getContent()).isNotNull();
        assertThat(pdfDocument.getContent().length).isGreaterThan(0);
    }



}