package com.trendyol.international.commission.invoice.api.model.dto;

import com.trendyol.international.commission.invoice.api.model.request.CommissionInvoiceCreateRequest;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CommissionInvoiceCreateDto {
    private Long sellerId;
    private Date jobExecutionDate;
    private String country;
    private String currency;

    public static CommissionInvoiceCreateDto fromCommissionInvoiceCreateRequest(CommissionInvoiceCreateRequest commissionInvoiceCreateRequest) {
        return CommissionInvoiceCreateDto.builder()
                .sellerId(commissionInvoiceCreateRequest.getSellerId())
                .jobExecutionDate(commissionInvoiceCreateRequest.getJobExecutionDate())
                .country(commissionInvoiceCreateRequest.getCountry())
                .currency(commissionInvoiceCreateRequest.getCurrency())
                .build();
    }
}
