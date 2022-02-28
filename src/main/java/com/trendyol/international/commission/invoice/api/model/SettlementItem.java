package com.trendyol.international.commission.invoice.api.model;

import com.trendyol.international.commission.invoice.api.model.base.AuditingEntity;
import com.trendyol.international.commission.invoice.api.model.enums.TransactionType;
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
@Table(name = "settlement_items")
@SequenceGenerator(name = "seq_settlement_items", sequenceName = "seq_settlement_items")
public class SettlementItem extends AuditingEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_settlement_items")
    private Long id;

    @Column(name = "order_number", nullable = false)
    private String orderNumber;

    @Column(name = "order_line_item_id", nullable = false)
    private Long orderLineItemId;

    @Column(name = "origin_id")
    private String originId;

    @Column(name = "shipment_package_id")
    private Long shipmentPackageId;

    @Column(name = "supplier_name", nullable = false)
    private String supplierName;

    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;

    @Column(name = "current_account_name")
    private String currentAccountName;

    @Column(name = "transaction_type_id")
    private TransactionType transactionType;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "barcode", nullable = false)
    private String barcode;

    @Column(name = "supplier_revenue")
    private BigDecimal supplierRevenue;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "commission")
    private BigDecimal commission;

    @Column(name = "commission_rate")
    private BigDecimal commissionRate;

    @Column(name = "involved_in_ps")
    private Boolean involvedInPS;

    @Column(name = "payment_period", nullable = false)
    private Integer paymentPeriod;

    @Column(name = "delivery_date")
    private Date deliveryDate;

    @Column(name = "payment_date")
    private Date paymentDate;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "with_initial_charge")
    private Boolean withInitialCharge;
}
