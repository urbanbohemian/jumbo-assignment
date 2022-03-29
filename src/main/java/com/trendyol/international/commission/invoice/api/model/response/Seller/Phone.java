package com.trendyol.international.commission.invoice.api.model.response.Seller;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Builder
@Data
public class Phone {
    private String countryCode;
    private String phone;

    public String getFullPhoneNumber() {
        return Objects.nonNull(countryCode) && Objects.nonNull(phone)
                ? countryCode.concat(phone)
                : StringUtils.EMPTY;
    }
}
