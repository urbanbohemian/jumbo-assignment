package com.trendyol.international.commission.invoice.api.model.response.Seller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VatNumber {
     private Date createdDate;
     private Date updateDate;
     private Integer id;
     private String vat; //NL-2112141264"
}
