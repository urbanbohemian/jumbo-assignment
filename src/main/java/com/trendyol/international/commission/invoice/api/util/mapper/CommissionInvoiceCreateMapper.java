package com.trendyol.international.commission.invoice.api.util.mapper;

import com.trendyol.international.commission.invoice.api.domain.event.CommissionInvoiceCreateEvent;
import com.trendyol.international.commission.invoice.api.model.dto.CommissionInvoiceCreateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommissionInvoiceCreateMapper {
    CommissionInvoiceCreateMapper INSTANCE = Mappers.getMapper(CommissionInvoiceCreateMapper.class);

    @Mappings({})
    CommissionInvoiceCreateDto commissionInvoiceCreateDto(CommissionInvoiceCreateEvent commissionInvoiceCreateEvent);
}