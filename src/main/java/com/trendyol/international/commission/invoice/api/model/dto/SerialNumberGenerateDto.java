package com.trendyol.international.commission.invoice.api.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class SerialNumberGenerateDto {
    private Long sellerId;
    private Date jobExecutionDate;
}
