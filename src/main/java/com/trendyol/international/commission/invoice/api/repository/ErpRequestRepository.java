package com.trendyol.international.commission.invoice.api.repository;

import com.trendyol.international.commission.invoice.api.domain.ErpRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpRequestRepository extends JpaRepository<ErpRequest, Long> {
}
