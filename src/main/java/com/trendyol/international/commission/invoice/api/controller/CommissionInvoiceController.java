package com.trendyol.international.commission.invoice.api.controller;

import com.trendyol.international.commission.invoice.api.service.CommissionInvoiceService;
import com.trendyol.international.commission.invoice.api.service.ShovelExceptionService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/commission-invoice")
public class CommissionInvoiceController {
    private final CommissionInvoiceService commissionInvoiceService;
    private final ShovelExceptionService shovelExceptionService;

    @PostMapping
    public void create() {
        log.info("Commission Invoice Create Job Execution is started.");
        commissionInvoiceService.create();
        log.info("Commission Invoice Create Job Execution is ended successfully.");
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

    @PostMapping("/shovel-exceptions")
    public void shovelExceptions(@RequestParam(value="retryableExceptionList") List<String> retryableExceptionList) {
        log.info("Commission Invoice Generate PDF Job Execution is started.");
        shovelExceptionService.shovelAllExceptions(retryableExceptionList);
        log.info("Commission Invoice Generate PDF Job Execution is ended successfully.");
    }
}
