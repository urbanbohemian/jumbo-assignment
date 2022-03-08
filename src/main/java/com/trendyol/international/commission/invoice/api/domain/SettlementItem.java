package com.trendyol.international.commission.invoice.api.domain;

import com.trendyol.international.commission.invoice.api.domain.base.AuditingEntity;
import com.trendyol.international.commission.invoice.api.model.enums.TransactionType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

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

    @Column(name = "item_creation_date", nullable = false)
    private Date itemCreationDate;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "transaction_type_id")
    private TransactionType transactionType;

    @Column(name = "commission_amount", nullable = false)
    private BigDecimal commissionAmount;

    @Column(name = "delivery_date")
    private Date deliveryDate;

    @Column(name = "payment_date")
    private Date paymentDate;

    public BigDecimal getCommissionAmountSignedValue() {
        return Objects.isNull(this.commissionAmount) ? BigDecimal.ZERO : this.commissionAmount.multiply(getTransactionType().getMultiplier());
    }

}
