package com.trendyol.international.commission.invoice.api.model.response.Seller;

import lombok.Data;

@Data
public class Address {
    private AddressType addressType;

    public String getAddressLine1() {
        return null;
    }

    public String getAddressLine2() {
        return null;
    }
}
