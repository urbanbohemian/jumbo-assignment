package com.trendyol.international.commission.invoice.api.service;

import com.trendyol.international.commission.invoice.api.domain.CommissionInvoice;
import com.trendyol.international.commission.invoice.api.repository.CommissionInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommissionInvoiceService {

    private final CommissionInvoiceRepository commissionInvoiceRepository;

    public CommissionInvoice saveCommissionInvoice(CommissionInvoice commissionInvoice) {
        return commissionInvoiceRepository.save(commissionInvoice);
    }
}
