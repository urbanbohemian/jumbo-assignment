package com.trendyol.international.commission.invoice.api.repository;

import com.trendyol.international.commission.invoice.api.domain.CommissionInvoice;
import com.trendyol.international.commission.invoice.api.model.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommissionInvoiceRepository extends JpaRepository<CommissionInvoice, Long> {

    CommissionInvoice findTopBySellerIdOrderByEndDateDesc(Long sellerId);

    List<CommissionInvoice> findBySellerIdAndInvoiceStatus(Long sellerId, InvoiceStatus invoiceStatus);
}
