package com.trendyol.international.commission.invoice.api.repository;

import com.trendyol.international.commission.invoice.api.model.CustomerInvoiceError;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerInvoiceErrorRepository extends CrudRepository<CustomerInvoiceError, String> {
    Page<CustomerInvoiceError> findByExceptionDetailIn(List<String> exceptionDetail, Pageable pageable);
}
