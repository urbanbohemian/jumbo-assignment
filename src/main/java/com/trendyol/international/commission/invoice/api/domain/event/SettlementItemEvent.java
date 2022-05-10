package com.trendyol.international.commission.invoice.api.domain.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.trendyol.international.commission.invoice.api.util.Hashing;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SettlementItemEvent {
    private Long id;
    private Date createdDate;
    private Long sellerId;
    private Integer transactionTypeId;
    private Date deliveryDate;
    private Date paymentDate;
    private BigDecimal totalCommission;
    private Long storeFrontId;
    private String currency;

    @JsonIgnore
    public String getHashId() {
        return Hashing.md5(Optional.ofNullable(id).orElse(0L).toString()
                        .concat(Optional.ofNullable(createdDate).orElse(new Date(1L)).toString())
                        .concat(Optional.ofNullable(sellerId).orElse(0L).toString())
                        .concat(Optional.ofNullable(transactionTypeId).orElse(0).toString())
                        .concat(Optional.ofNullable(deliveryDate).orElse(new Date(2L)).toString())
                        .concat(Optional.ofNullable(paymentDate).orElse(new Date(3L)).toString())
                        .concat(Optional.ofNullable(totalCommission).orElse(BigDecimal.ONE).toString())
                        .concat(Optional.ofNullable(storeFrontId).orElse(0L).toString())
                        .concat(Optional.ofNullable(currency).orElse("currency")));
    }
}