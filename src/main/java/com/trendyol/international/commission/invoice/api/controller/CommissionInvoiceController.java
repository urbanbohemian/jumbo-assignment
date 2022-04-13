package com.trendyol.international.commission.invoice.api.controller;

import com.trendyol.international.commission.invoice.api.service.CommissionInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/commission-invoice")
public class CommissionInvoiceController {
    private final CommissionInvoiceService commissionInvoiceService;

    @PostMapping
    public void create() {
        commissionInvoiceService.create();
    }

    @PostMapping("/generate-serial-number")
    public void generateSerialNumber() {
        commissionInvoiceService.generateSerialNumber();
    }

    @PostMapping("/generate-pdf")
    public void generatePdf() {
        commissionInvoiceService.generatePdf();
    }
}
