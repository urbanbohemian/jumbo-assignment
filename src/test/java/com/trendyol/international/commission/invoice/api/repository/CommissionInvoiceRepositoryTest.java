package com.trendyol.international.commission.invoice.api.repository;

import com.trendyol.international.commission.invoice.api.domain.CommissionInvoice;
import com.trendyol.international.commission.invoice.api.types.VatStatusType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
public class CommissionInvoiceRepositoryTest {

    @Autowired
    private CommissionInvoiceRepository commissionInvoiceRepository;

    @Test
    public void it_should_save_settlement_item() {
        //given
        CommissionInvoice commissionInvoice = CommissionInvoice.builder()
                .serialNumber("serial-number")
                .amount(BigDecimal.ONE)
                .netAmount(BigDecimal.ONE)
                .vatAmount(BigDecimal.ONE)
                .vatRate(BigDecimal.ONE)
                .vatStatusType(VatStatusType.DOMESTIC)
                .chargedVatDescription("charged-vat-description")
                .invoiceDate(new Date())
                .storeFrontId("store-front-id")
                .country("country")
                .currency("currency")
                .build();

        //when
        CommissionInvoice savedCommissionInvoice = commissionInvoiceRepository.save(commissionInvoice);

        //then
        assertThat(savedCommissionInvoice).isEqualTo(commissionInvoice);
    }
}