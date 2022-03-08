package com.trendyol.international.commission.invoice.api.controller;

import com.trendyol.international.commission.invoice.api.model.request.CommissionInvoiceCreateRequest;
import com.trendyol.international.commission.invoice.api.service.CommissionInvoiceService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public void create(@RequestBody CommissionInvoiceCreateRequest commissionInvoiceCreateRequest) {
        commissionInvoiceService.create(
                commissionInvoiceCreateRequest.getSellerId(),
                commissionInvoiceCreateRequest.getJobExecutionDate(),
                commissionInvoiceCreateRequest.getCountry(),
                commissionInvoiceCreateRequest.getCurrency());
    }

}
