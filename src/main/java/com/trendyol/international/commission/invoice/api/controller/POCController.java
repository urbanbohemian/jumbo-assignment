package com.trendyol.international.commission.invoice.api.controller;

import com.trendyol.international.commission.invoice.api.model.document.PDFDocument;
import com.trendyol.international.commission.invoice.api.model.dto.CommissionInvoice;
import com.trendyol.international.commission.invoice.api.service.PDFBoxService;
import com.trendyol.international.commission.invoice.api.service.POCService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_PDF;

@RestController
@RequestMapping("/poc")
public class POCController {

    private static final String ATTACHMENT_FILENAME = "attachment; filename=";

    private POCService pocService;

    private PDFBoxService pdfBoxService;

    public POCController(POCService pocService, PDFBoxService pdfBoxService) {
        this.pocService = pocService;
        this.pdfBoxService = pdfBoxService;
    }

    @PostMapping("/pdf")
    public ResponseEntity<Resource> createPDF(@RequestBody CommissionInvoice commissionInvoiceDTO) {

        PDFDocument pdf = pocService.createPDF(commissionInvoiceDTO);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT_FILENAME + pdf.getName())
                .contentType(APPLICATION_PDF)
                .contentLength(pdf.getContent().length)
                .body(new ByteArrayResource(pdf.getContent()));
    }

    @PostMapping("/pdfBox")
    public void createPDFBox() throws IOException {
        pdfBoxService.createPDF();
    }

}
