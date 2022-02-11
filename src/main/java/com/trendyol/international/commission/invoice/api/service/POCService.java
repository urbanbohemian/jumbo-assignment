package com.trendyol.international.commission.invoice.api.service;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.trendyol.international.commission.invoice.api.model.document.PDFDocument;
import com.trendyol.international.commission.invoice.api.model.dto.CommissionInvoiceDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Files;

@Service
public class POCService {

    public static final String DEST = "./target/htmlsamples/ch01/helloWorld01.pdf";

    public PDFDocument createPDF(CommissionInvoiceDTO commissionInvoiceDTO) {
        //For create the pdf directory
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        String htmlContent = getHtmlSource();

        htmlContent = pdfFiller(commissionInvoiceDTO,htmlContent);

        OutputStream outputStream = new ByteArrayOutputStream();

        //HtmlConverter.convertToPdf(htmlContent, new FileOutputStream(DEST));
        PDFDocument document = convertToPDFDocument(htmlContent);

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

        htmlContent = htmlContent.replace("{vatIdentificationNumber}",commissionInvoiceDTO.getVatIdentificationNumber());
        htmlContent = htmlContent.replace("{fullName}",commissionInvoiceDTO.getFullName());
        htmlContent = htmlContent.replace("{address}",commissionInvoiceDTO.getAddress());
        htmlContent = htmlContent.replace("{invoiceSerialNumber}",commissionInvoiceDTO.getSerialNumber());
        htmlContent = htmlContent.replace("{invoiceSerialNumber}",commissionInvoiceDTO.getSerialNumber());
        htmlContent = htmlContent.replace("{grossAmount}",commissionInvoiceDTO.getGrossAmount().toString());
        htmlContent = htmlContent.replace("{netAmount}",commissionInvoiceDTO.getNetAmount().toString());
        htmlContent = htmlContent.replace("{commissionAmount}",commissionInvoiceDTO.getCommissionAmount().toString());
        htmlContent = htmlContent.replace("{createdDate}",commissionInvoiceDTO.getCreatedDate().toString());

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



}
