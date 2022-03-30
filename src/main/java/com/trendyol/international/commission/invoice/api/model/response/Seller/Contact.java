package com.trendyol.international.commission.invoice.api.model.response.Seller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
    private String email;
    private Phone phone;
}
