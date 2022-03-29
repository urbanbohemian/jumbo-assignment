package com.trendyol.international.commission.invoice.api.domain.event;

import com.trendyol.international.commission.invoice.api.domain.InvoiceLine;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Builder
@Data
public class DocumentCreateMessage {
    private Long sellerId;
    private String sellerName;
    private String addressLine;
    private String email;
    private String phone;
    private String invoiceNumber;
    private String referenceId;
    private Date invoiceDate;
    private String taxIdentificationNumber;
    private String vatRegistrationNumber;
    private String vatStatusType;
    private List<InvoiceLine> invoiceLineList;
}
