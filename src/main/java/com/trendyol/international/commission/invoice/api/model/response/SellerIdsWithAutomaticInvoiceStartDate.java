package com.trendyol.international.commission.invoice.api.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerIdsWithAutomaticInvoiceStartDate {
    public Integer count;
    public List<SellerIdWithAutomaticInvoiceStartDate> sellerIds;
}
