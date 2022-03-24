package com.trendyol.international.commission.invoice.api.mapper;

import com.trendyol.international.commission.invoice.api.model.dto.SerialNumberGenerateDto;
import com.trendyol.international.commission.invoice.api.model.request.SerialNumberGenerateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SerialNumberGenerateMapper {
    SerialNumberGenerateMapper INSTANCE = Mappers.getMapper(SerialNumberGenerateMapper.class);

    @Mappings({
            @Mapping(target = "sellerId", source = "sellerId"),
            @Mapping(target = "jobExecutionDate", source = "jobExecutionDate"),
    })
    SerialNumberGenerateDto serialNumberGenerateDto(SerialNumberGenerateRequest serialNumberGenerateRequest);
}
