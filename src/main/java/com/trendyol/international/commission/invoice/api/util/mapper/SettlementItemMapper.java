package com.trendyol.international.commission.invoice.api.util.mapper;

import com.trendyol.international.commission.invoice.api.domain.event.SettlementItemEvent;
import com.trendyol.international.commission.invoice.api.model.dto.SettlementItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SettlementItemMapper {
    SettlementItemMapper INSTANCE = Mappers.getMapper(SettlementItemMapper.class);

    @Mappings({@Mapping(target = "transactionType", source = "transactionTypeId"), @Mapping(target = "commission", source = "totalCommission")})
    SettlementItemDto settlementItemDto(SettlementItemEvent settlementItemEvent);
}
