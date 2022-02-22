package com.trendyol.international.commission.invoice.api.service;

import com.trendyol.international.commission.invoice.api.model.document.PDFDocument;
import com.trendyol.international.commission.invoice.api.model.dto.CommissionInvoiceDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class POCServiceTest {

    @InjectMocks
    @Spy
    POCService pocService;

    @Test
    void it_should_create_pdf(){
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
        pdfDocument.setName("INTERNATIONAL_PDF");
        pdfDocument.setContent(new byte[]{1, 2, 3});


        when(pocService.getHtmlSource()).thenReturn("content");
        when(pocService.convertToPDFDocument("content")).thenReturn(pdfDocument);

        //then
        PDFDocument pdf = pocService.createPDF(commissionInvoiceDTO);

        assertThat(pdf).isNotNull();
        assertThat(pdf.getContent()).isEqualTo(pdfDocument.getContent());

    }

    @Test
    void it_should_get_html_source(){
        //given
        File htmlFile = null;
        String htmlContent = "";
        try {
            htmlFile = ResourceUtils.getFile("classpath:invoice.html");
            htmlContent = new String(Files.readAllBytes(htmlFile.toPath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //when
        String actualHtmlSource = pocService.getHtmlSource();

        assertThat(actualHtmlSource).isNotBlank();
        assertThat(actualHtmlSource).isEqualTo(htmlContent);
    }

    @Test
    void it_should_fill_html_source(){
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
        commissionInvoiceDTO.setCargoAmount(BigDecimal.valueOf(34.90));

        PDFDocument pdfDocument = new PDFDocument();
        pdfDocument.setName("INTERNATIONAL_PDF");
        pdfDocument.setContent(new byte[]{1, 2, 3});

        File htmlFile = null;
        String htmlContent = "";
        try {
            htmlFile = ResourceUtils.getFile("classpath:invoice.html");
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
        assertThat(filledHtml).doesNotContain("{grossAmount}");
    }

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