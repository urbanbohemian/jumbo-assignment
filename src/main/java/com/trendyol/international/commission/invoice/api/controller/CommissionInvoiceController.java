package com.trendyol.international.commission.invoice.api.controller;

import com.trendyol.international.commission.invoice.api.mapper.CommissionInvoiceCreateMapper;
import com.trendyol.international.commission.invoice.api.model.request.CommissionInvoiceCreateRequest;
import com.trendyol.international.commission.invoice.api.service.CommissionInvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/commission-invoice")
public class CommissionInvoiceController {
    private final CommissionInvoiceService commissionInvoiceService;

    @PostMapping
    public void create(@RequestBody CommissionInvoiceCreateRequest commissionInvoiceCreateRequest) {
        log.info("Commission Invoice Create Job Execution is started.");
        commissionInvoiceService.create(CommissionInvoiceCreateMapper.INSTANCE.commissionInvoiceCreateDto(commissionInvoiceCreateRequest));
        log.info("Commission Invoice Create Job Execution is started.");
    }

    @PostMapping("/generate-serial-number")
    public void generateSerialNumber() {
        log.info("Commission Invoice Generate Serial Number Job Execution is started.");
        commissionInvoiceService.generateSerialNumber();
        log.info("Commission Invoice Generate Serial Number Job Execution is ended successfully.");
    }

    @PostMapping("/generate-pdf")
    public void generatePdf() {
        log.info("Commission Invoice Generate PDF Job Execution is started.");
        commissionInvoiceService.generatePdf();
        log.info("Commission Invoice Generate PDF Job Execution is ended successfully.");
    }
}
