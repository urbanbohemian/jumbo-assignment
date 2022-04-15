package com.trendyol.international.commission.invoice.api.model;

import com.trendyol.international.commission.invoice.api.domain.base.AuditingEntity;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@Entity
@Table(name = "customer_invoice_errors")
public class CustomerInvoiceError extends AuditingEntity {
    @Id
    private String id;
    private String key;
    private String topic;
    private String exceptionDetail;
    @Type(type = "jsonb")
    @Column(name = "content", columnDefinition = "jsonb")
    private Object content;
}
