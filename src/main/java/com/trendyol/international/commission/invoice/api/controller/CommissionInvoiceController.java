package com.trendyol.international.commission.invoice.api.controller;

import com.trendyol.international.commission.invoice.api.kafka.failover.KafkaConsumerExceptionService;
import com.trendyol.international.commission.invoice.api.service.CommissionInvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/commission-invoice")
public class CommissionInvoiceController {
    private final CommissionInvoiceService commissionInvoiceService;
    private final KafkaConsumerExceptionService kafkaConsumerExceptionService;

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

    @PostMapping("/envelope")
    public void envelope() {
        log.info("Commission Invoice Envelope Job Execution is started.");
        commissionInvoiceService.envelope();
        log.info("Commission Invoice Envelope Job Execution is ended successfully.");
    }

    @PostMapping("/shovel-exceptions")
    public void shovelExceptions() throws ClassNotFoundException {
        log.info("Commission Invoice shovel exceptions job execution is started.");
        kafkaConsumerExceptionService.shovelExceptions();
        log.info("Commission Invoice shovel exceptions Job Execution is ended successfully.");
    }
}
