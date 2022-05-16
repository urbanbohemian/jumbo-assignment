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
    @JsonProperty("sellerId")
    public Long sellerId;
    @JsonProperty("automaticInvoiceStartDate")
    public Date automaticInvoiceStartDate;
    @JsonProperty("currency")
    public String currency;
    @JsonProperty("countryBasedIn")
    public String countryBasedIn;
}