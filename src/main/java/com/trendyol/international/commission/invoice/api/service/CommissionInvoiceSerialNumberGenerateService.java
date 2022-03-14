package com.trendyol.international.commission.invoice.api.service;

import com.trendyol.international.commission.invoice.api.domain.CommissionInvoiceNumberSequence;
import com.trendyol.international.commission.invoice.api.repository.CommissionInvoiceNumberSequenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommissionInvoiceSerialNumberGenerateService {

    private static final String SERIAL_NUMBER_PREFIX = "TBV";
    private static final String SEQUENCE_FORMAT = "%09d";
    private static final Integer SEQUENCE_INCREASE_SIZE = 1;

    private final CommissionInvoiceNumberSequenceRepository commissionInvoiceNumberSequenceRepository;

    public String generate(Integer invoiceYear) {
        CommissionInvoiceNumberSequence commissionInvoiceNumberSequence = getLatestCommissionInvoiceNumberSequence(invoiceYear);

        String serialNumber = SERIAL_NUMBER_PREFIX
                .concat(invoiceYear.toString())
                .concat(String.format(SEQUENCE_FORMAT, commissionInvoiceNumberSequence.getLatestSequence()));

        increaseLatestCommissionInvoiceNumberSequence(commissionInvoiceNumberSequence);

        return serialNumber;
    }

    private CommissionInvoiceNumberSequence getLatestCommissionInvoiceNumberSequence(Integer invoiceYear) {
        return commissionInvoiceNumberSequenceRepository
                .findBySerialKeyAndInvoiceYear(SERIAL_NUMBER_PREFIX, invoiceYear)
                .orElseGet(() -> createCommissionInvoiceNumberSequence(invoiceYear));
    }

    private CommissionInvoiceNumberSequence createCommissionInvoiceNumberSequence(Integer invoiceYear) {
        return commissionInvoiceNumberSequenceRepository.save(CommissionInvoiceNumberSequence.builder()
                .serialKey(SERIAL_NUMBER_PREFIX)
                .invoiceYear(invoiceYear)
                .latestSequence(0L)
                .build());
    }

    private void increaseLatestCommissionInvoiceNumberSequence(CommissionInvoiceNumberSequence commissionInvoiceNumberSequence) {
        commissionInvoiceNumberSequence.setLatestSequence(commissionInvoiceNumberSequence.getLatestSequence() + SEQUENCE_INCREASE_SIZE);
        commissionInvoiceNumberSequenceRepository.save(commissionInvoiceNumberSequence);
    }
}
