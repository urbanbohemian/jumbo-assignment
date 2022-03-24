package com.trendyol.international.commission.invoice.api.controller;

import com.trendyol.international.commission.invoice.api.model.dto.CommissionInvoiceCreateDto;
import com.trendyol.international.commission.invoice.api.model.request.CommissionInvoiceCreateRequest;
import com.trendyol.international.commission.invoice.api.service.CommissionInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/commission-invoice")
public class CommissionInvoiceController {
    private final CommissionInvoiceService commissionInvoiceService;

    @PostMapping
    public void create(@RequestBody CommissionInvoiceCreateRequest commissionInvoiceCreateRequest) {
        commissionInvoiceService.create(CommissionInvoiceCreateDto.fromCommissionInvoiceCreateRequest(commissionInvoiceCreateRequest));
    }
}
