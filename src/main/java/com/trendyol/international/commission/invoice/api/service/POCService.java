package com.trendyol.international.commission.invoice.api.service;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.trendyol.international.commission.invoice.api.model.document.PDFDocument;
import com.trendyol.international.commission.invoice.api.model.dto.CommissionInvoiceDTO;
import com.trendyol.international.commission.invoice.api.util.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.Objects;

@Service
public class POCService {

    public PDFDocument createPDF(CommissionInvoiceDTO commissionInvoiceDTO) {
        String htmlContent = getHtmlSource();

        htmlContent = pdfFiller(commissionInvoiceDTO,htmlContent);
        PDFDocument document = convertToPDFDocument(htmlContent);
        document.setName("International Commission Invoice");
        return document;
    }

    public String getHtmlSource() {
        File htmlFile = null;
        String htmlContent="";
        try {
            htmlFile = ResourceUtils.getFile("classpath:invoice.html");
            htmlContent = new String(Files.readAllBytes(htmlFile.toPath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return htmlContent;
    }

    public String pdfFiller(CommissionInvoiceDTO commissionInvoiceDTO, String htmlContent) {

        if(Objects.nonNull(commissionInvoiceDTO.getCargoAmount())) {
            htmlContent = htmlContent.replace("{cargoInvoice}",cargoInvoiceProcessor(commissionInvoiceDTO));
        }

        htmlContent = htmlContent.replace("{vatIdentificationNumber}",commissionInvoiceDTO.getVatIdentificationNumber());
        htmlContent = htmlContent.replace("{fullName}",commissionInvoiceDTO.getFullName());
        htmlContent = htmlContent.replace("{address}",commissionInvoiceDTO.getAddress());
        htmlContent = htmlContent.replace("{invoiceSerialNumber}",commissionInvoiceDTO.getSerialNumber());
        htmlContent = htmlContent.replace("{invoiceSerialNumber}",commissionInvoiceDTO.getSerialNumber());
        htmlContent = htmlContent.replace("{grossAmount}",commissionInvoiceDTO.getGrossAmount().toString());
        htmlContent = htmlContent.replace("{netAmount}",commissionInvoiceDTO.getNetAmount().toString());
        htmlContent = htmlContent.replace("{commissionAmount}",commissionInvoiceDTO.getCommissionAmount().toString());
        htmlContent = htmlContent.replace("{createdDate}", DateUtils.toStringTurkish(commissionInvoiceDTO.getCreatedDate()));

        return htmlContent;

    }

    public PDFDocument convertToPDFDocument(String htmlContent) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PDFDocument pdfDocument = new PDFDocument();
        try {
            HtmlConverter.convertToPdf(new ByteArrayInputStream(htmlContent.getBytes()), baos);
            pdfDocument.setContent(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pdfDocument;
    }

    public String cargoInvoiceProcessor(CommissionInvoiceDTO commissionInvoiceDTO) {
        return cargoInvoiceFiller(commissionInvoiceDTO,getCargoInvoiceHtml());
    }

    private String cargoInvoiceFiller(CommissionInvoiceDTO commissionInvoiceDTO, String cargoInvoiceHtml) {
        cargoInvoiceHtml = cargoInvoiceHtml.replace("{cargoAmount}",commissionInvoiceDTO.getCargoAmount().toString());
        return cargoInvoiceHtml;
    }

    private String getCargoInvoiceHtml() {
        File htmlFile = null;
        String htmlContent="";
        try {
            htmlFile = ResourceUtils.getFile("classpath:invoice-line.html");
            htmlContent = new String(Files.readAllBytes(htmlFile.toPath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return htmlContent;
    }
}