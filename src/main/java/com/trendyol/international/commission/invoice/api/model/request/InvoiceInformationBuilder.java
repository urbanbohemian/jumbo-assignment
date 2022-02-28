package com.trendyol.international.commission.invoice.api.model.request;

public final class InvoiceInformationBuilder {
    private String invoiceNumber;
    private String invoiceDate;
    private String dueDate;
    private String vatIdentificationNumber;
    private String vatIdentificationDate;

    private InvoiceInformationBuilder() {
    }

    public static InvoiceInformationBuilder anInvoiceInformation() {
        return new InvoiceInformationBuilder();
    }

    public InvoiceInformationBuilder invoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
        return this;
    }

    public InvoiceInformationBuilder invoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
        return this;
    }

    public InvoiceInformationBuilder dueDate(String dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public InvoiceInformationBuilder vatIdentificationNumber(String vatIdentificationNumber) {
        this.vatIdentificationNumber = vatIdentificationNumber;
        return this;
    }

    public InvoiceInformationBuilder vatIdentificationDate(String vatIdentificationDate) {
        this.vatIdentificationDate = vatIdentificationDate;
        return this;
    }

    public InvoiceInformation build() {
        InvoiceInformation invoiceInformation = new InvoiceInformation();
        invoiceInformation.setInvoiceNumber(invoiceNumber);
        invoiceInformation.setInvoiceDate(invoiceDate);
        invoiceInformation.setDueDate(dueDate);
        invoiceInformation.setVatIdentificationNumber(vatIdentificationNumber);
        invoiceInformation.setVatIdentificationDate(vatIdentificationDate);
        return invoiceInformation;
    }
}
