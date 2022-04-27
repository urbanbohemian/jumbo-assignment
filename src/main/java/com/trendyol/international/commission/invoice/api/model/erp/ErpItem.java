package com.trendyol.international.commission.invoice.api.model.erp;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ErpItem implements Serializable {
    private String productCode;
    private String amount;
    private String taxCode;
    private String businessArea;
}