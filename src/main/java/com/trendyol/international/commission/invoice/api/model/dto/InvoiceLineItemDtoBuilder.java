package com.trendyol.international.commission.invoice.api.model.dto;

import java.math.BigDecimal;

public final class InvoiceLineItemDtoBuilder {
    private String title;
    private BigDecimal amount;
    private String currency;

    private InvoiceLineItemDtoBuilder() {
    }

    public static InvoiceLineItemDtoBuilder anInvoiceLineItemDto() {
        return new InvoiceLineItemDtoBuilder();
    }

    public InvoiceLineItemDtoBuilder Title(String title) {
        this.title = title;
        return this;
    }

    public InvoiceLineItemDtoBuilder Amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public InvoiceLineItemDtoBuilder Currency(String currency) {
        this.currency = currency;
        return this;
    }

    public InvoiceLineItem build() {
        InvoiceLineItem invoiceLineItemDto = new InvoiceLineItem();
        invoiceLineItemDto.setTitle(title);
        invoiceLineItemDto.setAmount(amount);
        invoiceLineItemDto.setCurrency(currency);
        return invoiceLineItemDto;
    }
}
