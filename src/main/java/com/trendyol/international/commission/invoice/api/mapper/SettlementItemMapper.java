package com.trendyol.international.commission.invoice.api.mapper;

import com.trendyol.international.commission.invoice.api.domain.event.SettlementItemMessage;
import com.trendyol.international.commission.invoice.api.model.dto.SettlementItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SettlementItemMapper {
    SettlementItemMapper INSTANCE = Mappers.getMapper(SettlementItemMapper.class);

    @Mappings({@Mapping(target = "transactionType", source = "transactionTypeId"), @Mapping(target = "commission", source = "totalCommission")})
    SettlementItemDto settlementItemDto(SettlementItemMessage settlementItemMessage);
}
