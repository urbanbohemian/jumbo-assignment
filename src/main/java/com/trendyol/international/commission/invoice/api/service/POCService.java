package com.trendyol.international.commission.invoice.api.service;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.layout.font.FontProvider;
import com.trendyol.international.commission.invoice.api.model.document.PDFDocument;
import com.trendyol.international.commission.invoice.api.model.dto.CommissionInvoice;
import com.trendyol.international.commission.invoice.api.model.dto.InvoiceLineItem;
import com.trendyol.international.commission.invoice.api.util.DateUtils;
import org.springframework.cglib.core.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

@Service
public class POCService {

    public static final String FONT = "fonts/rubik-regular.ttf";

    public PDFDocument createPDF(CommissionInvoice commissionInvoice) {
        String htmlContent = getHtmlSource("invoice.html");
        htmlContent = pdfFiller(commissionInvoice, htmlContent);
        PDFDocument document = convertToPDFDocument(htmlContent);
        document.setName("International Commission Invoice");
        return document;
    }

    public String pdfFiller(CommissionInvoice commissionInvoice, String htmlContent) {
       /* htmlContent = htmlContent.replace("{title}", commissionInvoice.getVatIdentificationNumber());
        htmlContent = htmlContent.replace("{{genericReplaceValue}}", cargoInvoiceProcessor(commissionInvoice.getLineItems()));
        htmlContent = htmlContent.replace("{vatIdentificationNumber}", commissionInvoice.getVatIdentificationNumber());
        htmlContent = htmlContent.replace("{fullName}", commissionInvoice.getFullName());
        htmlContent = htmlContent.replace("{address}", commissionInvoice.getAddress());
        htmlContent = htmlContent.replace("{invoiceSerialNumber}", commissionInvoice.getSerialNumber());
        htmlContent = htmlContent.replace("{invoiceSerialNumber}", commissionInvoice.getSerialNumber());
        htmlContent = htmlContent.replace("{createdDate}", Objects.requireNonNull(DateUtils.toStringTurkish(commissionInvoice.getCreatedDate())));*/
        return htmlContent;
    }

    public PDFDocument convertToPDFDocument(String htmlContent) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PDFDocument pdfDocument = new PDFDocument();
        ConverterProperties cp = new ConverterProperties();
        FontProvider fontProvider = new DefaultFontProvider(false, false, false);
        fontProvider.addFont(FONT);
        cp.setFontProvider(fontProvider);

        try {
            HtmlConverter.convertToPdf(new ByteArrayInputStream(htmlContent.getBytes()), baos, cp);
            pdfDocument.setContent(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pdfDocument;
    }

    public String cargoInvoiceProcessor(List<InvoiceLineItem> invoiceLineItems) {
        return invoiceLineItems.stream()
                .map(invoiceLineItem -> cargoInvoiceFiller(invoiceLineItem, getHtmlSource("invoice-line.html")))
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
            File htmlFile = new ClassPathResource(resourceLocation).getFile();
            htmlContent = new String(Files.readAllBytes(htmlFile.toPath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return htmlContent;
    }
}