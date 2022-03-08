package com.trendyol.international.commission.invoice.api.controller;

import com.trendyol.international.commission.invoice.api.service.CommissionInvoiceService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/commission-invoice")
public class CommissionInvoiceController {

    private final CommissionInvoiceService commissionInvoiceService;

    public CommissionInvoiceController(CommissionInvoiceService commissionInvoiceService) {
        this.commissionInvoiceService = commissionInvoiceService;
    }

    @PostMapping
    public void processCreated() {
        commissionInvoiceService.create();
    }

}
