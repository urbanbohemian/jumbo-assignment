[International Commission Invoice API]()

This service creates commission invoices from settlements of sellers in specified periods.

[How To Get Settlements of Sellers?](https://lucid.app/lucidchart/2d18306c-e6eb-409d-9e8e-f3284e4863da/edit?invitationId=inv_db70d270-bbde-42fb-9603-b3c02105e05c&page=WeLZadbfEkM6#)

After each write operation in [settlements table in international-settlement DB](https://wiki.trendyol.com/pages/viewpage.action?spaceKey=SFS&title=International+Settlement+Models), event produced 
with [debezium](https://debezium.io/) to [kafka topic](https://c3-stretch-stage.mars.trendyol.com/clusters/mas0OX-RTISd9QPoxQR43A/management/topics/seller.settlement.int-settlement-debezium.settlement-cdc/overview) is consumed, and then settlement is saved in **settlement_items** table.

#### Life Cycle of Commission Invoice

[Creation](https://lucid.app/lucidchart/2d18306c-e6eb-409d-9e8e-f3284e4863da/edit?invitationId=inv_db70d270-bbde-42fb-9603-b3c02105e05c&page=VlT-w2EUvv0I#)

After POST request is sent to [/commission-invoice](https://stage-seller-finance-intcommissioninvoice-service.earth.trendyol.com/swagger-ui/index.html#/commission-invoice-controller/create) 
endpoint, event produced to [kafka topic](https://c3-stretch-stage.mars.trendyol.com/clusters/mas0OX-RTISd9QPoxQR43A/management/topics/seller.finance.internationalcommissioninvoiceapi.commissionInvoiceCreate.0/overview) to create a commission invoice for each seller that is included in the invoice received from [Seller Read API](https://stage-seller-base-international-seller-read-service.earth.trendyol.com/swagger-ui.html) is consumed, and then commission invoice is saved in 
**commission_invoices** table with calculating settlements of seller.

[Serial Number Generation](https://lucid.app/lucidchart/2d18306c-e6eb-409d-9e8e-f3284e4863da/edit?invitationId=inv_db70d270-bbde-42fb-9603-b3c02105e05c&page=FFEcfPza3xUY#)

After POST request is sent to [/commission-invoice/generate-serial-number](https://stage-seller-finance-intcommissioninvoice-service.earth.trendyol.com/swagger-ui/index.html#/commission-invoice-controller/generateSerialNumber) endpoint, for each commission invoice in CREATED status in **commission_invoices** table, serial number is generated and its status is updated as NUMBER_GENERATED.

[PDF Generation](https://lucid.app/lucidchart/2d18306c-e6eb-409d-9e8e-f3284e4863da/edit?invitationId=inv_db70d270-bbde-42fb-9603-b3c02105e05c&page=xxpeoENEp0sc#)

After POST request is sent to [/commission-invoice/generate-pdf](https://stage-seller-finance-intcommissioninvoice-service.earth.trendyol.com/swagger-ui/index.html#/commission-invoice-controller/generatePdf) endpoint, for each commission invoice in NUMBER_GENERATED status in **commission_invoices** table, event produced to 
[kafka topic](https://c3-stretch-stage.mars.trendyol.com/clusters/mas0OX-RTISd9QPoxQR43A/management/topics/seller.finance.internationalcommissioninvoiceapi.documentCreate.0/overview) to generate a 
PDF in [International Commission Invoice Document Consumer](https://gitlab.trendyol.com/seller/finance/international/international-commission-invoice-document-consumer).

[Envelopment](https://lucid.app/lucidchart/2d18306c-e6eb-409d-9e8e-f3284e4863da/edit?invitationId=inv_db70d270-bbde-42fb-9603-b3c02105e05c&page=n0JeW4nV.iTL#)

After POST request is sent to [/commission-invoice/envelope](https://stage-seller-finance-intcommissioninvoice-service.earth.trendyol.com/swagger-ui/index.html#/commission-invoice-controller/envelope) endpoint, for each commission invoice in PDF_GENERATED status in **commission_invoices** table, ERP Request is saved in **erp_requests** table and 
its status is updated as ENVELOPED.

- [Data Models](https://wiki.trendyol.com/display/SF/Commission+Invoice+Data+Models)
- [Configuration Project](https://gitlab.trendyol.com/seller/finance/cfg/international/international-commission-invoice-api)