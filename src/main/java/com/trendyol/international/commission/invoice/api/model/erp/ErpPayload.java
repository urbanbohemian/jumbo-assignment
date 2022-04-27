package com.trendyol.international.commission.invoice.api.model.erp;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class ErpPayload implements Serializable {
    private String companyCode;
    private String sellerId;
    private String documentNumber;
    private String documentDate;
    private String description;
    private String currency;
    private List<ErpItem> items;
}
