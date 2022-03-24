package com.trendyol.international.commission.invoice.api.model.request;

import lombok.Data;

import java.util.Date;

@Data
public class SerialNumberGenerateRequest {
    private Long sellerId;
    private Date jobExecutionDate;
}
