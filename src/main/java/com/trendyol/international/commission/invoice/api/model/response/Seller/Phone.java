package com.trendyol.international.commission.invoice.api.model.response.Seller;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Phone {
    private String countryCode;
    private String phone;

    public String getFullPhoneNumber(){
        return countryCode.concat(phone);
    }
}
