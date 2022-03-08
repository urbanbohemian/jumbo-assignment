package com.trendyol.international.commission.invoice.api.converter;

import com.trendyol.international.commission.invoice.api.model.enums.TransactionType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class TransactionTypeConverter
        implements AttributeConverter<TransactionType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TransactionType transactionType) {
        if (transactionType == null) {
            return null;
        }
        return transactionType.getId();
    }

    @Override
    public TransactionType convertToEntityAttribute(Integer id) {
        return TransactionType.from(id);
    }
}
