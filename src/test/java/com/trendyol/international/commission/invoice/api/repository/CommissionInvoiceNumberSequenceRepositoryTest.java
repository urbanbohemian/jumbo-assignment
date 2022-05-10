package com.trendyol.international.commission.invoice.api.repository;

import com.trendyol.international.commission.invoice.api.domain.entity.CommissionInvoiceNumberSequence;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
public class CommissionInvoiceNumberSequenceRepositoryTest {

    @Autowired
    private CommissionInvoiceNumberSequenceRepository commissionInvoiceNumberSequenceRepository;

    @Test
    public void it_should_save_commission_invoice_number_sequence() {
        //given
        CommissionInvoiceNumberSequence commissionInvoiceNumberSequence = CommissionInvoiceNumberSequence.builder()
                .serialKey("TBV")
                .invoiceYear(2022)
                .latestSequence(0L)
                .build();

        //when
        CommissionInvoiceNumberSequence savedCommissionInvoiceNumberSequence = commissionInvoiceNumberSequenceRepository
                .save(commissionInvoiceNumberSequence);

        //then
        assertThat(savedCommissionInvoiceNumberSequence).isEqualTo(commissionInvoiceNumberSequence);
    }

    @Test
    public void it_should_find_by_serial_key_and_invoice_year() {
        //given
        CommissionInvoiceNumberSequence commissionInvoiceNumberSequence = CommissionInvoiceNumberSequence.builder()
                .serialKey("TBV")
                .invoiceYear(2022)
                .latestSequence(0L)
                .build();

        commissionInvoiceNumberSequenceRepository.save(commissionInvoiceNumberSequence);

        //when
        Optional<CommissionInvoiceNumberSequence> commissionInvoiceNumberSequenceOpt = commissionInvoiceNumberSequenceRepository
                .findBySerialKeyAndInvoiceYear("TBV", 2022);

        //then
        assertThat(commissionInvoiceNumberSequenceOpt).isPresent();
        assertThat(commissionInvoiceNumberSequenceOpt.get()).isEqualTo(commissionInvoiceNumberSequence);
    }
}