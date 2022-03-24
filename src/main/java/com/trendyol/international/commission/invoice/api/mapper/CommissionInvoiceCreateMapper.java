package com.trendyol.international.commission.invoice.api.mapper;

import com.trendyol.international.commission.invoice.api.model.dto.CommissionInvoiceCreateDto;
import com.trendyol.international.commission.invoice.api.model.request.CommissionInvoiceCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommissionInvoiceCreateMapper {
    CommissionInvoiceCreateMapper INSTANCE = Mappers.getMapper(CommissionInvoiceCreateMapper.class);

    @Mappings({
            @Mapping(target = "sellerId", source = "sellerId"),
            @Mapping(target = "jobExecutionDate", source = "jobExecutionDate"),
            @Mapping(target = "country", source = "country"),
            @Mapping(target = "currency", source = "currency"),
    })
    CommissionInvoiceCreateDto commissionInvoiceCreateDto(CommissionInvoiceCreateRequest commissionInvoiceCreateRequest);
}
