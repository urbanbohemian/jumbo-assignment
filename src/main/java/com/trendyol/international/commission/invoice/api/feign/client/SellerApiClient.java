package com.trendyol.international.commission.invoice.api.feign.client;

import com.trendyol.international.commission.invoice.api.config.feign.FeignConfig;
import com.trendyol.international.commission.invoice.api.feign.domain.response.Pageable;
import com.trendyol.international.commission.invoice.api.model.response.SellerIdWithAutomaticInvoiceStartDate;
import com.trendyol.international.commission.invoice.api.model.response.SellerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "sellerApiClient", url = "${application.urls.seller-api}", configuration = FeignConfig.class)
public interface SellerApiClient {

    @GetMapping("/seller/{sellerId}")
    SellerResponse getSellerById(@PathVariable("sellerId") long sellerId);

    @GetMapping("/mp-seller/finance/weekly-automatic-invoice-enabled-by-page")
    Pageable<SellerIdWithAutomaticInvoiceStartDate> getWeeklyInvoiceEnabledSellers(@RequestParam Integer page, @RequestParam Integer size);
}
