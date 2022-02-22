package com.trendyol.international.commission.invoice.api.model.dto;

import java.math.BigDecimal;

public class InvoiceLineItem {
    private String title;
    private BigDecimal amount;
    private String currency;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
