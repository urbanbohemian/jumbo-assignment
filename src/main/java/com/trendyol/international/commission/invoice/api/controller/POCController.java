package com.trendyol.international.commission.invoice.api.controller;

import com.trendyol.international.commission.invoice.api.model.document.PDFDocument;
import com.trendyol.international.commission.invoice.api.model.dto.CommissionInvoiceDTO;
import com.trendyol.international.commission.invoice.api.service.POCService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_PDF;

@RestController
@RequestMapping("/poc")
public class POCController {

    private POCService pocService;

    public POCController(POCService pocService) {
        this.pocService = pocService;
    }

    @PostMapping("/pdf")
    public ResponseEntity<Resource> createPDF(@RequestBody CommissionInvoiceDTO commissionInvoiceDTO) {

        PDFDocument pdf = pocService.createPDF(commissionInvoiceDTO);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "INTERNATIONAL_PDF_TEST")
                .contentType(APPLICATION_PDF)
                .contentLength(pdf.getContent().length)
                .body(new ByteArrayResource(pdf.getContent()));
    }

}
