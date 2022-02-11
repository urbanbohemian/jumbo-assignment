package com.trendyol.international.commission.invoice.api.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public class CommissionInvoiceDTO {

    private String serialNumber;
    private String vatIdentificationNumber;
    private String fullName;
    private String address;
    private Date createdDate;
    private BigDecimal grossAmount;
    private BigDecimal netAmount;
    private BigDecimal commissionAmount;

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

    public BigDecimal getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public BigDecimal getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(BigDecimal commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    @Override
    public String toString() {
        return "CommissionInvoiceDTO{" +
                "serialNumber='" + serialNumber + '\'' +
                ", vatIdentificationNumber='" + vatIdentificationNumber + '\'' +
                ", fullName='" + fullName + '\'' +
                ", address='" + address + '\'' +
                ", createdDate=" + createdDate +
                ", grossAmount=" + grossAmount +
                ", netAmount=" + netAmount +
                ", commissionAmount=" + commissionAmount +
                '}';
    }
}
