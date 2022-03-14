package com.trendyol.international.commission.invoice.api.repository;

import com.trendyol.international.commission.invoice.api.domain.CommissionInvoiceNumberSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommissionInvoiceNumberSequenceRepository extends JpaRepository<CommissionInvoiceNumberSequence, Long> {

    Optional<CommissionInvoiceNumberSequence> findBySerialKeyAndInvoiceYear(String serialKey, Integer invoiceYear);
}
