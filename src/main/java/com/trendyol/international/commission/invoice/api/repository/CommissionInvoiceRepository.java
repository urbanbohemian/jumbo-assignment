package com.trendyol.international.commission.invoice.api.repository;

import com.trendyol.international.commission.invoice.api.domain.entity.CommissionInvoice;
import com.trendyol.international.commission.invoice.api.model.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommissionInvoiceRepository extends JpaRepository<CommissionInvoice, Long> {

    Optional<CommissionInvoice> findTopBySellerIdOrderByEndDateDesc(Long sellerId);

    List<CommissionInvoice> findByInvoiceStatus(InvoiceStatus invoiceStatus);
}
