package com.trendyol.international.commission.invoice.api.repository;

import com.trendyol.international.commission.invoice.api.domain.CommissionInvoice;
import com.trendyol.international.commission.invoice.api.model.enums.InvoiceStatus;
import com.trendyol.international.commission.invoice.api.model.enums.VatStatusType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
public class CommissionInvoiceRepositoryTest {

    @Autowired
    private CommissionInvoiceRepository commissionInvoiceRepository;

    @Test
    public void it_should_save_commission_invoice() {
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
                .sellerId(1L)
                .startDate(new Date())
                .endDate(new Date(1L))
                .invoiceStatus(InvoiceStatus.CREATED)
                .build();

        //when
        CommissionInvoice savedCommissionInvoice = commissionInvoiceRepository.save(commissionInvoice);

        //then
        assertThat(savedCommissionInvoice).isEqualTo(commissionInvoice);
    }

    @Test
    public void it_should_find_top_commission_invoice_by_seller_id_end_date_desc() {
        //given
        CommissionInvoice commissionInvoice1 = CommissionInvoice.builder()
                .serialNumber("1")
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
                .sellerId(1L)
                .startDate(new Date())
                .endDate(new Date(1L))
                .invoiceStatus(InvoiceStatus.CREATED)
                .build();

        CommissionInvoice commissionInvoice2 = CommissionInvoice.builder()
                .serialNumber("2")
                .amount(BigDecimal.ONE)
                .netAmount(BigDecimal.ONE)
                .vatAmount(BigDecimal.ONE)
                .vatRate(BigDecimal.ONE)
                .vatStatusType(VatStatusType.DOMESTIC)
                .chargedVatDescription("charged-vat-description")
                .invoiceDate(new Date(3L))
                .storeFrontId("store-front-id")
                .country("country")
                .currency("currency")
                .sellerId(2L)
                .startDate(new Date())
                .endDate(new Date(3L))
                .invoiceStatus(InvoiceStatus.CREATED)
                .build();

        CommissionInvoice commissionInvoice3 = CommissionInvoice.builder()
                .serialNumber("3")
                .amount(BigDecimal.ONE)
                .netAmount(BigDecimal.ONE)
                .vatAmount(BigDecimal.ONE)
                .vatRate(BigDecimal.ONE)
                .vatStatusType(VatStatusType.DOMESTIC)
                .chargedVatDescription("charged-vat-description")
                .invoiceDate(new Date(2L))
                .storeFrontId("store-front-id")
                .country("country")
                .currency("currency")
                .sellerId(1L)
                .startDate(new Date())
                .endDate(new Date(2L))
                .invoiceStatus(InvoiceStatus.CREATED)
                .build();

        commissionInvoiceRepository.saveAll(List.of(commissionInvoice1, commissionInvoice2, commissionInvoice3));
        //when
        CommissionInvoice commissionInvoice = commissionInvoiceRepository.findTopBySellerIdOrderByEndDateDesc(1L);
        //then
        assertThat(commissionInvoice).isNotNull();
        assertThat(commissionInvoice.getSerialNumber()).isEqualTo("3");
    }

    @Test
    public void it_should_find_by_seller_id_and_invoice_status() {
        //given
        CommissionInvoice commissionInvoice1 = CommissionInvoice.builder()
                .serialNumber("1")
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
                .sellerId(1L)
                .startDate(new Date())
                .endDate(new Date(1L))
                .invoiceStatus(InvoiceStatus.CREATED)
                .build();

        CommissionInvoice commissionInvoice2 = CommissionInvoice.builder()
                .serialNumber("2")
                .amount(BigDecimal.ONE)
                .netAmount(BigDecimal.ONE)
                .vatAmount(BigDecimal.ONE)
                .vatRate(BigDecimal.ONE)
                .vatStatusType(VatStatusType.DOMESTIC)
                .chargedVatDescription("charged-vat-description")
                .invoiceDate(new Date(3L))
                .storeFrontId("store-front-id")
                .country("country")
                .currency("currency")
                .sellerId(1L)
                .startDate(new Date())
                .endDate(new Date(3L))
                .invoiceStatus(InvoiceStatus.CREATED)
                .build();

        CommissionInvoice commissionInvoice3 = CommissionInvoice.builder()
                .serialNumber("3")
                .amount(BigDecimal.ONE)
                .netAmount(BigDecimal.ONE)
                .vatAmount(BigDecimal.ONE)
                .vatRate(BigDecimal.ONE)
                .vatStatusType(VatStatusType.DOMESTIC)
                .chargedVatDescription("charged-vat-description")
                .invoiceDate(new Date(2L))
                .storeFrontId("store-front-id")
                .country("country")
                .currency("currency")
                .sellerId(2L)
                .startDate(new Date())
                .endDate(new Date(2L))
                .invoiceStatus(InvoiceStatus.CREATED)
                .build();

        CommissionInvoice commissionInvoice4 = CommissionInvoice.builder()
                .serialNumber("4")
                .amount(BigDecimal.ONE)
                .netAmount(BigDecimal.ONE)
                .vatAmount(BigDecimal.ONE)
                .vatRate(BigDecimal.ONE)
                .vatStatusType(VatStatusType.DOMESTIC)
                .chargedVatDescription("charged-vat-description")
                .invoiceDate(new Date(2L))
                .storeFrontId("store-front-id")
                .country("country")
                .currency("currency")
                .sellerId(1L)
                .startDate(new Date())
                .endDate(new Date(2L))
                .invoiceStatus(InvoiceStatus.NUMBER_GENERATED)
                .build();

        commissionInvoiceRepository.saveAll(List.of(commissionInvoice1, commissionInvoice2, commissionInvoice3, commissionInvoice4));
        //when
        List<CommissionInvoice> commissionInvoices = commissionInvoiceRepository.findBySellerIdAndInvoiceStatus(1L, InvoiceStatus.CREATED);
        //then
        assertThat(commissionInvoices.size()).isEqualTo(2);
        assertThat(commissionInvoices.stream().map(CommissionInvoice::getSerialNumber).toList()).contains("1", "2");
    }
}