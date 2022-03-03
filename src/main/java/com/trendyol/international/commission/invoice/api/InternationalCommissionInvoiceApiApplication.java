package com.trendyol.international.commission.invoice.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableConfigurationProperties
public class InternationalCommissionInvoiceApiApplication {
//	private final POCController pocController;
//
//	public InternationalCommissionInvoiceApiApplication(PDFBoxService pdfBoxService, POCController pocController) {
//		this.pocController = pocController;
//	}

	public static void main(String[] args) {
		SpringApplication.run(InternationalCommissionInvoiceApiApplication.class, args);
	}

//	@Override
//	public void run(String... args) {
//		CommissionInvoiceRequest commissionInvoiceRequest = new CommissionInvoiceRequest();
//		commissionInvoiceRequest.setSellerInfo(SellerInfoBuilder.aSellerInfo().title("Inditex-OKAN").addressLine1("addressLine1-OKAN").addressLine2("addressLine2-OKAN").email("abcd@gmail.com").phone("+905221233458").build());
//		commissionInvoiceRequest.setInvoiceInformation(InvoiceInformationBuilder.anInvoiceInformation().invoiceDate("01-02-2022").invoiceNumber("DSM12345").dueDate("05-02-2022").vatIdentificationDate("10-10-2022").vatIdentificationNumber("TR12345678").build());
//
//		List<CommissionInvoiceRequest> commissionInvoiceRequestList = List.of(commissionInvoiceRequest);
//		this.pocController.createPDFBox(commissionInvoiceRequestList);
//	}
}