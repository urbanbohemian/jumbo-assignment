package com.trendyol.international.commission.invoice.api.util;

import com.trendyol.international.commission.invoice.api.model.request.CommissionInvoiceRequest;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class PDFModelFactory {
    private final PdfConfig pdfConfig;
    public Map<String, PdfComponent> pdfModelMapTemplate;

    @PostConstruct
    public void init() {
        this.pdfModelMapTemplate = pdfConfig.getComponents();
    }

    public PDFModelFactory(PdfConfig pdfConfig) {
        this.pdfConfig = pdfConfig;
    }

    public Map<String, PdfComponent> createPdfComponentMap (CommissionInvoiceRequest commissionInvoiceRequest)  {
        pdfModelMapTemplate.get("seller-title").setResourceValue(commissionInvoiceRequest.getSellerInfo().getTitle());
        pdfModelMapTemplate.get("address-line1").setResourceValue(commissionInvoiceRequest.getSellerInfo().getAddressLine1());
        pdfModelMapTemplate.get("address-line2").setResourceValue(commissionInvoiceRequest.getSellerInfo().getAddressLine2());
        pdfModelMapTemplate.get("seller-mail").setResourceValue(commissionInvoiceRequest.getSellerInfo().getEmail());
        pdfModelMapTemplate.get("seller-phone").setResourceValue(commissionInvoiceRequest.getSellerInfo().getPhone());
        pdfModelMapTemplate.get("invoice-number").setResourceValue(commissionInvoiceRequest.getInvoiceInformation().getInvoiceNumber());
        pdfModelMapTemplate.get("invoice-date").setResourceValue(commissionInvoiceRequest.getInvoiceInformation().getInvoiceDate());
        pdfModelMapTemplate.get("due-date").setResourceValue(commissionInvoiceRequest.getInvoiceInformation().getDueDate());
        pdfModelMapTemplate.get("vat-id-number").setResourceValue(commissionInvoiceRequest.getInvoiceInformation().getVatIdentificationNumber());
        pdfModelMapTemplate.get("vat-registration-date").setResourceValue(commissionInvoiceRequest.getInvoiceInformation().getVatIdentificationDate());
        return pdfModelMapTemplate;
    }
}
