package com.trendyol.international.commission.invoice.api.repository;

import com.trendyol.international.commission.invoice.api.AbstractPostgresContainer;
import com.trendyol.international.commission.invoice.api.domain.entity.CommissionInvoice;
import com.trendyol.international.commission.invoice.api.model.enums.InvoiceStatus;
import com.trendyol.international.commission.invoice.api.model.enums.VatStatusType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommissionInvoiceRepositoryTest extends AbstractPostgresContainer {

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
                .description("charged-vat-description")
                .invoiceDate(new Date())
                .storeFrontId("store-front-id")
                .invoiceTypeId("invoice-type-unique-id")
                .country("country")
                .currency("currency")
                .sellerId(1L)
                .startDate(new Date())
                .endDate(new Date(1L))
                .invoiceStatus(InvoiceStatus.CREATED)
                .referenceId("reference-id")
                .settlementCount(1)
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
                .serialNumber("serial-number-1")
                .amount(BigDecimal.ONE)
                .netAmount(BigDecimal.ONE)
                .vatAmount(BigDecimal.ONE)
                .vatRate(BigDecimal.ONE)
                .vatStatusType(VatStatusType.DOMESTIC)
                .description("charged-vat-description")
                .invoiceDate(new Date())
                .storeFrontId("store-front-id")
                .invoiceTypeId("invoice-type-unique-id")
                .country("country")
                .currency("currency")
                .sellerId(1L)
                .startDate(new Date())
                .endDate(new Date(1L))
                .invoiceStatus(InvoiceStatus.CREATED)
                .referenceId("reference-id-1")
                .settlementCount(1)
                .build();

        CommissionInvoice commissionInvoice2 = CommissionInvoice.builder()
                .serialNumber("serial-number-2")
                .amount(BigDecimal.ONE)
                .netAmount(BigDecimal.ONE)
                .vatAmount(BigDecimal.ONE)
                .vatRate(BigDecimal.ONE)
                .vatStatusType(VatStatusType.DOMESTIC)
                .description("charged-vat-description")
                .invoiceDate(new Date(3L))
                .storeFrontId("store-front-id")
                .invoiceTypeId("invoice-type-unique-id")
                .country("country")
                .currency("currency")
                .sellerId(2L)
                .startDate(new Date())
                .endDate(new Date(3L))
                .invoiceStatus(InvoiceStatus.CREATED)
                .referenceId("reference-id-2")
                .settlementCount(1)
                .build();

        CommissionInvoice commissionInvoice3 = CommissionInvoice.builder()
                .serialNumber("serial-number-3")
                .amount(BigDecimal.ONE)
                .netAmount(BigDecimal.ONE)
                .vatAmount(BigDecimal.ONE)
                .vatRate(BigDecimal.ONE)
                .vatStatusType(VatStatusType.DOMESTIC)
                .description("charged-vat-description")
                .invoiceDate(new Date(2L))
                .storeFrontId("store-front-id")
                .invoiceTypeId("invoice-type-unique-id")
                .country("country")
                .currency("currency")
                .sellerId(1L)
                .startDate(new Date())
                .endDate(new Date(2L))
                .invoiceStatus(InvoiceStatus.CREATED)
                .referenceId("reference-id-3")
                .settlementCount(1)
                .build();

        commissionInvoiceRepository.saveAll(List.of(commissionInvoice1, commissionInvoice2, commissionInvoice3));
        //when
        Optional<CommissionInvoice> commissionInvoiceOptional = commissionInvoiceRepository.findTopBySellerIdOrderByEndDateDesc(1L);
        //then
        assertThat(commissionInvoiceOptional).isPresent();
        assertThat(commissionInvoiceOptional.get().getSerialNumber()).isEqualTo("serial-number-3");
    }

    @Test
    public void it_should_find_by_invoice_status() {
        //given
        CommissionInvoice commissionInvoice1 = CommissionInvoice.builder()
                .serialNumber("serial-number-1")
                .amount(BigDecimal.ONE)
                .netAmount(BigDecimal.ONE)
                .vatAmount(BigDecimal.ONE)
                .vatRate(BigDecimal.ONE)
                .vatStatusType(VatStatusType.DOMESTIC)
                .description("charged-vat-description")
                .invoiceDate(new Date())
                .storeFrontId("store-front-id")
                .invoiceTypeId("invoice-type-unique-id")
                .country("country")
                .currency("currency")
                .sellerId(1L)
                .startDate(new Date())
                .endDate(new Date(1L))
                .invoiceStatus(InvoiceStatus.CREATED)
                .referenceId("reference-id-1")
                .settlementCount(1)
                .build();

        CommissionInvoice commissionInvoice2 = CommissionInvoice.builder()
                .serialNumber("serial-number-2")
                .amount(BigDecimal.ONE)
                .netAmount(BigDecimal.ONE)
                .vatAmount(BigDecimal.ONE)
                .vatRate(BigDecimal.ONE)
                .vatStatusType(VatStatusType.DOMESTIC)
                .description("charged-vat-description")
                .invoiceDate(new Date(3L))
                .storeFrontId("store-front-id")
                .invoiceTypeId("invoice-type-unique-id")
                .country("country")
                .currency("currency")
                .sellerId(1L)
                .startDate(new Date())
                .endDate(new Date(3L))
                .invoiceStatus(InvoiceStatus.CREATED)
                .referenceId("reference-id-2")
                .settlementCount(2)
                .build();

        CommissionInvoice commissionInvoice3 = CommissionInvoice.builder()
                .serialNumber("serial-number-3")
                .amount(BigDecimal.ONE)
                .netAmount(BigDecimal.ONE)
                .vatAmount(BigDecimal.ONE)
                .vatRate(BigDecimal.ONE)
                .vatStatusType(VatStatusType.DOMESTIC)
                .description("charged-vat-description")
                .invoiceDate(new Date(2L))
                .storeFrontId("store-front-id")
                .invoiceTypeId("invoice-type-unique-id")
                .country("country")
                .currency("currency")
                .sellerId(2L)
                .startDate(new Date())
                .endDate(new Date(2L))
                .invoiceStatus(InvoiceStatus.CREATED)
                .referenceId("reference-id-3")
                .settlementCount(3)
                .build();

        CommissionInvoice commissionInvoice4 = CommissionInvoice.builder()
                .serialNumber("serial-number-4")
                .amount(BigDecimal.ONE)
                .netAmount(BigDecimal.ONE)
                .vatAmount(BigDecimal.ONE)
                .vatRate(BigDecimal.ONE)
                .vatStatusType(VatStatusType.DOMESTIC)
                .description("charged-vat-description")
                .invoiceDate(new Date(2L))
                .storeFrontId("store-front-id")
                .invoiceTypeId("invoice-type-unique-id")
                .country("country")
                .currency("currency")
                .sellerId(1L)
                .startDate(new Date())
                .endDate(new Date(2L))
                .invoiceStatus(InvoiceStatus.NUMBER_GENERATED)
                .referenceId("reference-id-4")
                .settlementCount(1)
                .build();

        commissionInvoiceRepository.saveAll(List.of(commissionInvoice1, commissionInvoice2, commissionInvoice3, commissionInvoice4));
        //when
        List<CommissionInvoice> commissionInvoices = commissionInvoiceRepository.findByInvoiceStatus(InvoiceStatus.CREATED);
        //then
        assertThat(commissionInvoices.size()).isEqualTo(3);
        assertThat(commissionInvoices.stream().map(CommissionInvoice::getSerialNumber).toList()).contains("serial-number-1", "serial-number-2", "serial-number-3");
    }
}