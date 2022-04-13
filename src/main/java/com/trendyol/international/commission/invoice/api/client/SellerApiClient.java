package com.trendyol.international.commission.invoice.api.client;

import com.trendyol.international.commission.invoice.api.config.feign.FeignConfig;
import com.trendyol.international.commission.invoice.api.model.response.SellerIdsWithAutomaticInvoiceStartDate;
import com.trendyol.international.commission.invoice.api.model.response.SellerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "sellerApiClient", url = "${application.urls.seller-api}", configuration = FeignConfig.class)
public interface SellerApiClient {

    @GetMapping("/seller/{sellerId}")
    SellerResponse getSellerById(@PathVariable("sellerId") long sellerId);

    @GetMapping("/mp-seller/finance/weekly-automatic-invoice-enabled")
    SellerIdsWithAutomaticInvoiceStartDate getWeeklyInvoiceEnabledSellers();
}
