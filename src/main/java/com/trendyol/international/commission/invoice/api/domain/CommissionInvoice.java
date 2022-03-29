package com.trendyol.international.commission.invoice.api.domain;

import com.trendyol.international.commission.invoice.api.domain.base.AuditingEntity;
import com.trendyol.international.commission.invoice.api.model.enums.InvoiceStatus;
import com.trendyol.international.commission.invoice.api.model.enums.VatStatusType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@SuperBuilder
@Entity
@SequenceGenerator(name = "seq_commission_invoices", sequenceName = "seq_commission_invoices")
public class CommissionInvoice extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_commission_invoices")
    private Long id;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "serial_number", nullable = false, unique = true)
    private String serialNumber;                                    //article 'b'

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

    @Column(name = "invoice_date", nullable = false)
    private Date invoiceDate;

    @Column(name = "store_front_id", nullable = false)
    private String storeFrontId;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "status", nullable = false)
    private InvoiceStatus invoiceStatus;

    @Column(name = "reference_id", nullable = false, unique = true)
    private String referenceId;

    @OneToMany
    @JoinTable(
            name = "commission_invoice_settlement_items",
            joinColumns = {@JoinColumn(name = "commission_invoice_id")},
            inverseJoinColumns = {@JoinColumn(name = "settlement_item_id")},
            uniqueConstraints = @UniqueConstraint(columnNames = {"settlement_item_id"})

    )
    private Set<SettlementItem> settlementItems;
}
