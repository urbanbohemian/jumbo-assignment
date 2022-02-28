package com.trendyol.international.commission.invoice.api.model;

import com.trendyol.international.commission.invoice.api.types.VATStatusType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "commission_invoices")
public class CommissionInvoice {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "created_date")
    private Date createdDate;                                       //article 'a'

    @Column(name = "serial_number")
    private String serialNumber;                                    //article 'b'

    @Column(name = "seller_vat_id")
    private String sellerVAT;                                       //article 'c'

    @Column(name = "seller_full_name")
    private String sellerFullName;                                  //article 'd'

    @Column(name = "seller_address")
    private String sellerAddress;                                   //article 'd'

    @Column(name = "delivery_date")
    private Date deliveryDate;                                      //article 'e'

    @Column(name = "amount")
    private BigDecimal amount;                                      //article 'f'

    @Column(name = "net_amount")
    private BigDecimal netAmount;                                   //article 'g'

    @Column(name = "vat_amount")
    private BigDecimal vatAmount;                                   //article 'h'

    @Column(name = "vat_rate")
    private BigDecimal vatRate;                                     //article 'h'

    @Column(name = "vat_status_type")
    private VATStatusType vatStatusType;                            //article 'n'

    @Column(name = "charged_vat_description")
    private String chargedVATDescription;                           //article 'n'


}
