package com.trendyol.international.commission.invoice.api.model.response.Seller;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Builder
@Data
public class Address {
    private static final String SLASH = "/";

    private String addressLine;
    private AddressType addressType;
    private String country;
    private String district;

    public String getFormattedAddress() {
        return addressLine.concat(StringUtils.SPACE).concat(district).concat(SLASH).concat(country);
    }
}
