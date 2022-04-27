package com.trendyol.international.commission.invoice.api.domain;


import com.trendyol.international.commission.invoice.api.model.erp.ErpPayload;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@Table(name = "erp_requests")
@SequenceGenerator(name = "seq_erp_requests", sequenceName = "seq_erp_requests")
public class ErpRequest implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_erp_requests")
    private Long id;

    @Column(name = "seller_id")
    private Long sellerId;

    @Column(name = "document_number")
    private String documentNumber;

    @Column(name = "document_date")
    private Date documentDate;

    @Type(type = "jsonb")
    @Column(name = "payload", columnDefinition = "jsonb")
    private ErpPayload payload;
}