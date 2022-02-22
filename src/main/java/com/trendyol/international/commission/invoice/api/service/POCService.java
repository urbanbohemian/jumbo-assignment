package com.trendyol.international.commission.invoice.api.service;

import com.itextpdf.html2pdf.HtmlConverter;
import com.trendyol.international.commission.invoice.api.model.document.PDFDocument;
import com.trendyol.international.commission.invoice.api.model.dto.CommissionInvoice;
import com.trendyol.international.commission.invoice.api.model.dto.InvoiceLineItem;
import com.trendyol.international.commission.invoice.api.util.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

@Service
public class POCService {

    public PDFDocument createPDF(CommissionInvoice commissionInvoice) {
        String htmlContent = getHtmlSource("classpath:invoice.html");
        htmlContent = pdfFiller(commissionInvoice, htmlContent);
        PDFDocument document = convertToPDFDocument(htmlContent);
        document.setName("International Commission Invoice");
        return document;
    }

    public String pdfFiller(CommissionInvoice commissionInvoice, String htmlContent) {
        htmlContent = htmlContent.replace("{title}", commissionInvoice.getVatIdentificationNumber());
        htmlContent = htmlContent.replace("{{genericReplaceValue}}", cargoInvoiceProcessor(commissionInvoice.getLineItems()));
        htmlContent = htmlContent.replace("{vatIdentificationNumber}", commissionInvoice.getVatIdentificationNumber());
        htmlContent = htmlContent.replace("{fullName}", commissionInvoice.getFullName());
        htmlContent = htmlContent.replace("{address}", commissionInvoice.getAddress());
        htmlContent = htmlContent.replace("{invoiceSerialNumber}", commissionInvoice.getSerialNumber());
        htmlContent = htmlContent.replace("{invoiceSerialNumber}", commissionInvoice.getSerialNumber());
        htmlContent = htmlContent.replace("{createdDate}", Objects.requireNonNull(DateUtils.toStringTurkish(commissionInvoice.getCreatedDate())));
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

    public String cargoInvoiceProcessor(List<InvoiceLineItem> invoiceLineItems) {
        return invoiceLineItems.stream()
                .map(invoiceLineItem -> cargoInvoiceFiller(invoiceLineItem, getHtmlSource("classpath:invoice-line.html")))
                .reduce(String::concat)
                .orElse("");
    }

    private String cargoInvoiceFiller(InvoiceLineItem invoiceLineItemDto, String invoiceLineHtmlTemplate) {
        return invoiceLineHtmlTemplate
                .replace("{title}", invoiceLineItemDto.getTitle())
                .replace("{currency}", invoiceLineItemDto.getCurrency())
                .replace("{amount}", invoiceLineItemDto.getAmount().toString());
    }

    public String getHtmlSource(String resourceLocation) {
        String htmlContent = "";
        try {
            File htmlFile = ResourceUtils.getFile(resourceLocation);
            htmlContent = new String(Files.readAllBytes(htmlFile.toPath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return htmlContent;
    }
}