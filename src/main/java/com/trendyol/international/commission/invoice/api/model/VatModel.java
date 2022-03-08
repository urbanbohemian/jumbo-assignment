package com.trendyol.international.commission.invoice.api.model;

import java.math.BigDecimal;

public final class VatModel {

    private final BigDecimal vatRate;
    private final BigDecimal amount;
    private final BigDecimal vatAmount;
    private final BigDecimal netAmount;

    public VatModel(BigDecimal vatRate, BigDecimal amount, BigDecimal vatAmount, BigDecimal netAmount) {
        this.vatRate = vatRate;
        this.amount = amount;
        this.vatAmount = vatAmount;
        this.netAmount = netAmount;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }
}
