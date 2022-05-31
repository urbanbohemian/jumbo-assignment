package com.trendyol.international.commission.invoice.api.model.response;
import java.util.ArrayList;

import com.trendyol.international.commission.invoice.api.model.response.Seller.Address;
import com.trendyol.international.commission.invoice.api.model.response.Seller.AddressType;
import com.trendyol.international.commission.invoice.api.model.response.Seller.MasterUser;
import com.trendyol.international.commission.invoice.api.model.response.Seller.VatNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Slf4j
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerResponse {
    private String companyName;
    private List<Address> addresses;
    private MasterUser masterUser;
    private String taxNumber;
    private String countryBasedIn;
    private List<VatNumber> vatNumberList;
    private String registrationNumber;

    public Optional<Address> getInvoiceAddress() {
        return addresses.stream().filter(f -> AddressType.INVOICE_ADDRESS.equals(f.getAddressType())).findFirst();
    }

    public String getVatRegistrationNumber() {
    String vatRegistrationNumber = !ObjectUtils.isNotEmpty(countryBasedIn) || !ObjectUtils.isNotEmpty(vatNumberList) ? StringUtils.EMPTY : vatNumberList
                .stream()
                .filter(vatNumber -> vatNumber.getVat().split("-")[0].equalsIgnoreCase(countryBasedIn))
                .map(VatNumber::getVat).findFirst().orElse(StringUtils.EMPTY);
        log.info("countryBasedIn: {}",countryBasedIn);
        log.info("vatNumberList: {}",vatNumberList);
        log.info("vat-value is {} ",vatRegistrationNumber);
        return vatRegistrationNumber;
    }
}