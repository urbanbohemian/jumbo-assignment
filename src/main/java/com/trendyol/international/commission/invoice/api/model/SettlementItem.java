package com.trendyol.international.commission.invoice.api.model;

import com.trendyol.international.commission.invoice.api.model.base.AuditingEntity;
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

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "commission", nullable = false)
    private BigDecimal commission;

    @Column(name = "delivery_date")
    private Date deliveryDate;

    @Column(name = "payment_date")
    private Date paymentDate;

    @Column(name = "item_creation_date")
    private Date itemCreationDate;
}
