package com.trendyol.international.commission.invoice.api.model.response;

import com.trendyol.international.commission.invoice.api.model.response.Seller.Address;
import com.trendyol.international.commission.invoice.api.model.response.Seller.AddressType;
import com.trendyol.international.commission.invoice.api.model.response.Seller.MasterUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

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

    public Optional<Address> getInvoiceAddress() {
        return addresses.stream().filter(f -> AddressType.INVOICE_ADDRESS.equals(f.getAddressType())).findFirst();
    }
}
