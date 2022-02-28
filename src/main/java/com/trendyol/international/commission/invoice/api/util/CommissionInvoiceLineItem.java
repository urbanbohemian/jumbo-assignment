package com.trendyol.international.commission.invoice.api.util;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Builder
@Data
public class CommissionInvoiceLineItem {
    private String description;
    private Integer quantity;
    private String unit;
    private Float unitPrice;
    private Float vatRate;
    private Float amount;

    public List<String> getCellValues() {
        return List.of(description,String.valueOf(quantity),unit,String.valueOf(unitPrice), "%" + vatRate,String.valueOf(amount));
    }
}
