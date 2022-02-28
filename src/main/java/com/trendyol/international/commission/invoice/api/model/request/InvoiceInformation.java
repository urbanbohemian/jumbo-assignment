package com.trendyol.international.commission.invoice.api.model.request;

public class InvoiceInformation {
    private String invoiceNumber;
    private String invoiceDate;
    private String dueDate;
    private String vatIdentificationNumber;
    private String vatIdentificationDate;

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getVatIdentificationNumber() {
        return vatIdentificationNumber;
    }

    public void setVatIdentificationNumber(String vatIdentificationNumber) {
        this.vatIdentificationNumber = vatIdentificationNumber;
    }

    public String getVatIdentificationDate() {
        return vatIdentificationDate;
    }

    public void setVatIdentificationDate(String vatIdentificationDate) {
        this.vatIdentificationDate = vatIdentificationDate;
    }

    @Override
    public String toString() {
        return "InvoiceInformation{" +
                "invoiceNumber='" + invoiceNumber + '\'' +
                ", invoiceDate='" + invoiceDate + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", vatIdentificationNumber='" + vatIdentificationNumber + '\'' +
                ", vatIdentificationDate='" + vatIdentificationDate + '\'' +
                '}';
    }
}
