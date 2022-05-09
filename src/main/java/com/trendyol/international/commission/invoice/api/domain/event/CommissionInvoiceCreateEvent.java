package com.trendyol.international.commission.invoice.api.domain.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trendyol.international.commission.invoice.api.util.Hashing;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Optional;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CommissionInvoiceCreateEvent {
    private Long sellerId;
    private String country;
    private String currency;
    private Date automaticInvoiceStartDate;
    private Date endDate;

    @JsonIgnore
    public String getHashId() {
        log.info(Optional.ofNullable(sellerId).orElse(0L).toString()
                .concat(Optional.ofNullable(country).orElse(""))
                .concat(Optional.ofNullable(currency).orElse(""))
                .concat(Optional.ofNullable(automaticInvoiceStartDate).orElse(new Date(1L)).toString())
                .concat(Optional.ofNullable(endDate).orElse(new Date(1L)).toString()));
        return Hashing.md5(Optional.ofNullable(sellerId).orElse(0L).toString()
                .concat(Optional.ofNullable(country).orElse(""))
                .concat(Optional.ofNullable(currency).orElse(""))
                .concat(Optional.ofNullable(automaticInvoiceStartDate).orElse(new Date(1L)).toString())
                .concat(Optional.ofNullable(endDate).orElse(new Date(1L)).toString()));
    }
}
