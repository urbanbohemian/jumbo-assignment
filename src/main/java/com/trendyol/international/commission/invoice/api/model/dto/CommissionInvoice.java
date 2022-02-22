package com.trendyol.international.commission.invoice.api.model.dto;

import java.util.Date;
import java.util.List;

public class CommissionInvoice {

    private String serialNumber;
    private String vatIdentificationNumber;
    private String fullName;
    private String address;
    private Date createdDate;
    private List<InvoiceLineItem> lineItems;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getVatIdentificationNumber() {
        return vatIdentificationNumber;
    }

    public void setVatIdentificationNumber(String vatIdentificationNumber) {
        this.vatIdentificationNumber = vatIdentificationNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public List<InvoiceLineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<InvoiceLineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public CommissionInvoice() {
    }

    @Override
    public String toString() {
        return "CommissionInvoice{" +
                "serialNumber='" + serialNumber + '\'' +
                ", vatIdentificationNumber='" + vatIdentificationNumber + '\'' +
                ", fullName='" + fullName + '\'' +
                ", address='" + address + '\'' +
                ", createdDate=" + createdDate +
                ", lineItems=" + lineItems +
                '}';
    }
}
