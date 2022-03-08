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

    // Collect all settlement items and processSettlementItems
    public void create() {
        // Fetch all settlementItems in active period from DB
        // group up these settlement items by seller_id`s
        // For each seller in the group >> create commissionInvoice entity
        //                              >>   make relation between these items and commissionInvoice entity, attach these settlement items to CommissionInvoiceItems
        // save CommissionInvoice entity
    }


}
