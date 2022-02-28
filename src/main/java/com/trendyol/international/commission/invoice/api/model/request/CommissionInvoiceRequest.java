package com.trendyol.international.commission.invoice.api.model.request;

public class CommissionInvoiceRequest {
    private SellerInfo sellerInfo;
    private InvoiceInformation invoiceInformation;

    public SellerInfo getSellerInfo() {
        return sellerInfo;
    }

    public void setSellerInfo(SellerInfo sellerInfo) {
        this.sellerInfo = sellerInfo;
    }

    public InvoiceInformation getInvoiceInformation() {
        return invoiceInformation;
    }

    public void setInvoiceInformation(InvoiceInformation invoiceInformation) {
        this.invoiceInformation = invoiceInformation;
    }
}
