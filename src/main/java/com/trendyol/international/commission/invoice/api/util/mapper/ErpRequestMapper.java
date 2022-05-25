package com.trendyol.international.commission.invoice.api.util.mapper;

import com.trendyol.international.commission.invoice.api.domain.entity.CommissionInvoice;
import com.trendyol.international.commission.invoice.api.domain.entity.ErpRequest;
import com.trendyol.international.commission.invoice.api.model.erp.ErpItem;
import com.trendyol.international.commission.invoice.api.model.erp.ErpPayload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ErpRequestMapper {

    public ErpRequest mapEntityToErpRequest(CommissionInvoice commissionInvoice) {
        return ErpRequest.builder()
                .documentDate(commissionInvoice.getInvoiceDate())
                .documentNumber(commissionInvoice.getSerialNumber())
                .sellerId(commissionInvoice.getSellerId())
                .payload(ErpPayload.builder()
                        .companyCode("T005")
                        .documentDate(commissionInvoice.getInvoiceDate().toInstant().toString())
                        .sellerId(commissionInvoice.getSellerId().toString())
                        .documentNumber(commissionInvoice.getSerialNumber())
                        .currency(commissionInvoice.getCurrency())
                        .description(commissionInvoice.getDescription())
                        .items(List.of(ErpItem.builder()
                                .amount(commissionInvoice.getNetAmount().toString())
                                .productCode("productCode")
                                .taxCode(commissionInvoice.getVatRate().toString())
                                .businessArea("MP")
                                .build()))
                        .build())
                .build();
    }

}