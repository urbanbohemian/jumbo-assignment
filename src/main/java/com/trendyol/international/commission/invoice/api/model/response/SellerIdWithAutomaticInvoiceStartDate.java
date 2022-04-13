package com.trendyol.international.commission.invoice.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerIdWithAutomaticInvoiceStartDate {
    @JsonProperty("s")
    public Long sellerId;
    @JsonProperty("d")
    public Date automaticInvoiceStartDate;
}
