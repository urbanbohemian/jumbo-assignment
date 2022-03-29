package com.trendyol.international.commission.invoice.api.model.response.Seller;

import lombok.Data;

@Data
public class Contact {
    private String email;
    private Phone phone;

    public String getPhone() {
        return phone.getCountryCode().concat(phone.getPhone());
    }
}
