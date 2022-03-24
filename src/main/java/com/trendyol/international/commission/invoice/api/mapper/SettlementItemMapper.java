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

    @Mappings({
            @Mapping(target = "settlementItemId", source = "settlementItemId"),
            @Mapping(target = "sellerId", source = "sellerId"),
            @Mapping(target = "commission", source = "totalCommission"),
            @Mapping(target = "deliveryDate", source = "deliveryDate"),
            @Mapping(target = "paymentDate", source = "paymentDate"),
            @Mapping(target = "createdDate", source = "createdDate"),
            @Mapping(target = "transactionType", source = "type"),
            @Mapping(target = "storeFrontId", source = "storeFrontId"),
            @Mapping(target = "currency", source = "currency"),
    })
    SettlementItemDto settlementItemDto(SettlementItemMessage settlementItemMessage);
}
