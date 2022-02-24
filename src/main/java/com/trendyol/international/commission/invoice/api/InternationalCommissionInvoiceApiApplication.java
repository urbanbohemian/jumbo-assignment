package com.trendyol.international.commission.invoice.api;

import com.trendyol.international.commission.invoice.api.service.PDFBoxService;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
@EnableConfigurationProperties
public class InternationalCommissionInvoiceApiApplication implements CommandLineRunner{
	public InternationalCommissionInvoiceApiApplication(PDFBoxService pdfBoxService) {
		this.pdfBoxService = pdfBoxService;
	}

	public static void main(String[] args) {
		SpringApplication.run(InternationalCommissionInvoiceApiApplication.class, args);
	}

	private final PDFBoxService pdfBoxService;

	@Override
	public void run(String... args) throws Exception {
		this.pdfBoxService.createPDF();
	}
}