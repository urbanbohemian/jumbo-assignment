package com.trendyol.international.commission.invoice.api.domain.entity;

import com.trendyol.international.commission.invoice.api.domain.base.AuditingEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "commission_invoice_number_sequences", uniqueConstraints = @UniqueConstraint(columnNames = {"serial_key", "invoice_year"}))
@SequenceGenerator(name = "seq_commission_invoice_number_sequences", sequenceName = "seq_commission_invoice_number_sequences")
public class CommissionInvoiceNumberSequence extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_commission_invoice_number_sequences")
    private Long id;

    @Column(name = "serial_key", nullable = false, updatable = false, length = 3)
    private String serialKey;

    @Column(name = "invoice_year", nullable = false, updatable = false, length = 4)
    private Integer invoiceYear;

    @Column(name = "latest_sequence", nullable = false)
    private Long latestSequence;
}
