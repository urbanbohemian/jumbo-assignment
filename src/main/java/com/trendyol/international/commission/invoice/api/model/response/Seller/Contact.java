package com.trendyol.international.commission.invoice.api.model.response.Seller;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Contact {
    private String email;
    private Phone phone;
}
