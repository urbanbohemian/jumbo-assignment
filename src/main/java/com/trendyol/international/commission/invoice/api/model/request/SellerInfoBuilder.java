package com.trendyol.international.commission.invoice.api.model.request;

public final class SellerInfoBuilder {
    private String title;
    private String addressLine1;
    private String addressLine2;
    private String email;
    private String phone;

    private SellerInfoBuilder() {
    }

    public static SellerInfoBuilder aSellerInfo() {
        return new SellerInfoBuilder();
    }

    public SellerInfoBuilder title(String title) {
        this.title = title;
        return this;
    }

    public SellerInfoBuilder addressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
        return this;
    }

    public SellerInfoBuilder addressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
        return this;
    }

    public SellerInfoBuilder email(String email) {
        this.email = email;
        return this;
    }

    public SellerInfoBuilder phone(String phone) {
        this.phone = phone;
        return this;
    }

    public SellerInfo build() {
        SellerInfo sellerInfo = new SellerInfo();
        sellerInfo.setTitle(title);
        sellerInfo.setAddressLine1(addressLine1);
        sellerInfo.setAddressLine2(addressLine2);
        sellerInfo.setEmail(email);
        sellerInfo.setPhone(phone);
        return sellerInfo;
    }
}
