package com.trendyol.international.commission.invoice.api.model.enums;

import java.util.Arrays;

public enum InvoiceStatus {
    CREATED(1),
    NUMBER_GENERATED(51),
    PDF_GENERATED(101),
    ENVELOPED(151);

    InvoiceStatus(int id) {
        this.id = id;
    }
    private final int id;

    public static InvoiceStatus from(Integer id) {
        return Arrays.stream(values())
                .filter(invoiceStatus -> invoiceStatus.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Illegal " + InvoiceStatus.class.getSimpleName() + " id: " + id));
    }

    public Integer getId() {
        return this.id;
    }
}
