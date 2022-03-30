package com.trendyol.international.commission.invoice.api.model.response.Seller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private static final String SLASH = "/";

    private String addressLine;
    private AddressType addressType;
    private String country;
    private String district;

    public String getFormattedAddress() {
        return Objects.nonNull(addressLine) && Objects.nonNull(district) && Objects.nonNull(country)
                ? addressLine.concat(StringUtils.SPACE).concat(district).concat(SLASH).concat(country)
                : StringUtils.EMPTY;
    }
}
