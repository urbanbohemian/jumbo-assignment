package com.trendyol.international.commission.invoice.api.model;

import com.trendyol.international.commission.invoice.api.model.base.AuditingEntity;
import com.trendyol.international.commission.invoice.api.types.VatStatusType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "commission_invoices")
@SequenceGenerator(name = "seq_commission_invoices", sequenceName = "seq_commission_invoices")
public class CommissionInvoice extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_commission_invoices")
    private Long id;

    @Column(name = "serial_number", nullable = false)
    private String serialNumber;                                    //article 'b'

    @Column(name = "seller_vat_id", nullable = false)
    private String sellerVatId;                                     //article 'c'

    @Column(name = "seller_full_name", nullable = false)
    private String sellerFullName;                                  //article 'd'

    @Column(name = "seller_address", nullable = false)
    private String sellerAddress;                                   //article 'd'

    @Column(name = "delivery_date", nullable = false)
    private Date deliveryDate;                                      //article 'e'

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;                                      //article 'f'

    @Column(name = "net_amount", nullable = false)
    private BigDecimal netAmount;                                   //article 'g'

    @Column(name = "vat_amount", nullable = false)
    private BigDecimal vatAmount;                                   //article 'h'

    @Column(name = "vat_rate", nullable = false)
    private BigDecimal vatRate;                                     //article 'h'

    @Column(name = "vat_status_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private VatStatusType vatStatusType;                            //article 'n'

    @Column(name = "charged_vat_description", nullable = false)
    private String chargedVatDescription;                           //article 'n'

    @Column(name = "payment_date", nullable = false)
    private Date paymentDate;

    @Column(name = "store_front_id", nullable = false)
    private String storeFrontId;
}
