package com.trendyol.international.commission.invoice.api.converter;

import com.trendyol.international.commission.invoice.api.model.enums.InvoiceStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class InvoiceStatusConverter implements AttributeConverter<InvoiceStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(InvoiceStatus invoiceStatus) {
        if (invoiceStatus == null) {
            return null;
        }
        return invoiceStatus.getId();
    }

    @Override
    public InvoiceStatus convertToEntityAttribute(Integer id) {
        return InvoiceStatus.from(id);
    }
}