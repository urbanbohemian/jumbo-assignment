package com.trendyol.international.commission.invoice.api.service;

import com.trendyol.international.commission.invoice.api.domain.CommissionInvoice;
import com.trendyol.international.commission.invoice.api.domain.SettlementItem;
import com.trendyol.international.commission.invoice.api.model.VatModel;
import com.trendyol.international.commission.invoice.api.model.dto.CommissionInvoiceDto;
import com.trendyol.international.commission.invoice.api.model.enums.InvoiceStatus;
import com.trendyol.international.commission.invoice.api.model.enums.TransactionType;
import com.trendyol.international.commission.invoice.api.repository.CommissionInvoiceRepository;
import com.trendyol.international.commission.invoice.api.repository.SettlementItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommissionInvoiceServiceTest {

    @InjectMocks
    private CommissionInvoiceService commissionInvoiceService;

    @Mock
    private CommissionInvoiceRepository commissionInvoiceRepository;

    @Mock
    private SettlementItemRepository settlementItemRepository;

    @Mock
    private VatCalculatorService vatCalculatorService;

    @Test
    public void it_should_save_commission_invoice() {
        //given
        CommissionInvoice commissionInvoice = new CommissionInvoice();

        //when
        commissionInvoiceService.saveCommissionInvoice(commissionInvoice);

        //then
        verify(commissionInvoiceRepository).save(commissionInvoice);
    }

    @Test
    public void it_should_calculate_commission_for_seller_with_sale_and_return() {
        // given
        SettlementItem settlementItem1 = SettlementItem
                .builder()
                .transactionType(TransactionType.Return)
                .commissionAmount(BigDecimal.valueOf(100L))
                .itemCreationDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        SettlementItem settlementItem2 = SettlementItem
                .builder()
                .transactionType(TransactionType.Sale)
                .commissionAmount(BigDecimal.valueOf(90L))
                .itemCreationDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        Date startDate = Date.from(LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());

        // when
        BigDecimal commission = commissionInvoiceService.calculateCommissionForSeller(
                CommissionInvoiceDto
                        .builder()
                        .sellerId(2L)
                        .startDate(startDate)
                        .endDate(endDate)
                        .settlementItems(List.of(settlementItem1, settlementItem2)).build());

        // then
        assertThat(commission.compareTo(BigDecimal.valueOf(-10L))).isEqualTo(0);
    }

    @Test
    public void it_should_calculate_commission_for_seller_with_sale() {
        // given
        SettlementItem settlementItem1 = SettlementItem
                .builder()
                .transactionType(TransactionType.Sale)
                .commissionAmount(BigDecimal.valueOf(100L))
                .itemCreationDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        SettlementItem settlementItem2 = SettlementItem
                .builder()
                .transactionType(TransactionType.Sale)
                .commissionAmount(BigDecimal.valueOf(90L))
                .itemCreationDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        Date startDate = Date.from(LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());

        // when
        BigDecimal commission = commissionInvoiceService.calculateCommissionForSeller(
                CommissionInvoiceDto
                        .builder()
                        .sellerId(2L)
                        .startDate(startDate)
                        .endDate(endDate)
                        .settlementItems(List.of(settlementItem1, settlementItem2)).build());

        // then
        assertThat(commission.compareTo(BigDecimal.valueOf(190L))).isEqualTo(0);
    }

    @Test
    public void it_should_get_start_date_for_commission_invoice() {
        //given
        Date date = new Date(1646728032672L);
        CommissionInvoice commissionInvoice = CommissionInvoice
                .builder()
                .endDate(date)
                .sellerId(1L)
                .build();

        when(commissionInvoiceRepository.findTopBySellerIdOrderByEndDateDesc(1L)).thenReturn(commissionInvoice);
        //when
        Date startDate = commissionInvoiceService.getStartDateForSeller(1L);
        //then
        assertThat(startDate.getTime() - date.getTime()).isEqualTo(1);
    }

    @Test
    public void it_should_get_end_date_for_commission_invoice() {
        //given
        Date date = new Date(1646693999998L);
        //when
        Date endDate = commissionInvoiceService.getEndDate(date);
        //then
        assertThat(endDate.getTime() - date.getTime()).isEqualTo(1);
    }

    @Test
    public void it_should_get_end_date_for_commission_invoice_of_1_march() {
        //given
        Date date = new Date(1646096399000L);
        //when
        Date endDate = commissionInvoiceService.getEndDate(date);
        //then
        assertThat(endDate.getTime()).isEqualTo(1646089199999L);
    }

    @Test
    public void it_should_create_commission_invoice() {
        //given
        SettlementItem settlement1 = SettlementItem.builder()
                .sellerId(1L)
                .commissionAmount(BigDecimal.valueOf(121))
                .deliveryDate(new Date())
                .paymentDate(new Date())
                .itemCreationDate(new Date())
                .transactionType(TransactionType.Sale)
                .build();

        SettlementItem settlement2 = SettlementItem.builder()
                .sellerId(1L)
                .commissionAmount(BigDecimal.valueOf(121))
                .deliveryDate(new Date())
                .paymentDate(new Date())
                .transactionType(TransactionType.Sale)
                .itemCreationDate(new Date())
                .build();

        when(settlementItemRepository.findBySellerIdAndItemCreationDateBetween(eq(1L), any(), any())).thenReturn(List.of(settlement1, settlement2));
        VatModel vatModel = new VatModel(BigDecimal.valueOf(21), BigDecimal.valueOf(242), BigDecimal.valueOf(42), BigDecimal.valueOf(200));
        when(vatCalculatorService.calculateVatModel(BigDecimal.valueOf(242), BigDecimal.valueOf(21))).thenReturn(vatModel);
        //when
        commissionInvoiceService.create(1L, new Date(), "NL", "EUR");
        //then
        ArgumentCaptor<CommissionInvoice> commissionInvoiceCaptor = ArgumentCaptor.forClass(CommissionInvoice.class);
        verify(commissionInvoiceRepository).save(commissionInvoiceCaptor.capture());
        CommissionInvoice actualCommissionInvoice = commissionInvoiceCaptor.getValue();
        assertThat(actualCommissionInvoice.getAmount().compareTo(BigDecimal.valueOf(242))).isEqualTo(0);
        assertThat(actualCommissionInvoice.getNetAmount().compareTo(BigDecimal.valueOf(200))).isEqualTo(0);
        assertThat(actualCommissionInvoice.getVatAmount().compareTo(BigDecimal.valueOf(42))).isEqualTo(0);
        assertThat(actualCommissionInvoice.getCountry()).isEqualTo("NL");
        assertThat(actualCommissionInvoice.getCurrency()).isEqualTo("EUR");
        assertThat(actualCommissionInvoice.getInvoiceStatus()).isEqualTo(InvoiceStatus.CREATED);
    }

    @Test
    public void it_should_not_create_commission_invoice_when_there_is_no_settlement() {
        //given
        when(settlementItemRepository.findBySellerIdAndItemCreationDateBetween(eq(1L), any(), any())).thenReturn(List.of());
        //when
        commissionInvoiceService.create(1L, new Date(), "NL", "EUR");
        //then
        verify(commissionInvoiceRepository, never()).save(any());
    }

    @Test
    public void it_should_not_create_commission_invoice_when_total_amount_is_negative() {
        //given
        SettlementItem settlement1 = SettlementItem.builder()
                .sellerId(1L)
                .commissionAmount(BigDecimal.valueOf(121))
                .deliveryDate(new Date())
                .paymentDate(new Date())
                .itemCreationDate(new Date())
                .transactionType(TransactionType.Sale)
                .build();

        SettlementItem settlement2 = SettlementItem.builder()
                .sellerId(1L)
                .commissionAmount(BigDecimal.valueOf(122))
                .deliveryDate(new Date())
                .paymentDate(new Date())
                .transactionType(TransactionType.Return)
                .itemCreationDate(new Date())
                .build();
        when(settlementItemRepository.findBySellerIdAndItemCreationDateBetween(eq(1L), any(), any())).thenReturn(List.of(settlement1, settlement2));

        //when
        commissionInvoiceService.create(1L, new Date(), "NL", "EUR");
        //then
        verify(commissionInvoiceRepository, never()).save(any());
        verifyNoInteractions(vatCalculatorService);
    }

    @Test
    public void it_should_not_create_commission_invoice_when_total_amount_is_zero() {
        //given
        SettlementItem settlement1 = SettlementItem.builder()
                .sellerId(1L)
                .commissionAmount(BigDecimal.valueOf(122))
                .deliveryDate(new Date())
                .paymentDate(new Date())
                .itemCreationDate(new Date())
                .transactionType(TransactionType.Sale)
                .build();

        SettlementItem settlement2 = SettlementItem.builder()
                .sellerId(1L)
                .commissionAmount(BigDecimal.valueOf(122))
                .deliveryDate(new Date())
                .paymentDate(new Date())
                .transactionType(TransactionType.Return)
                .itemCreationDate(new Date())
                .build();
        when(settlementItemRepository.findBySellerIdAndItemCreationDateBetween(eq(1L), any(), any())).thenReturn(List.of(settlement1, settlement2));

        //when
        commissionInvoiceService.create(1L, new Date(), "NL", "EUR");
        //then
        verify(commissionInvoiceRepository, never()).save(any());
        verifyNoInteractions(vatCalculatorService);
    }

    @Test
    @DisplayName("Create commission invoice with a start date of 1ms after the end date of the previous invoice")
    public void it_should_create_commission_invoice_1ms_after_the_previous_invoice() {
        //given
        SettlementItem settlement1 = SettlementItem.builder()
                .sellerId(1L)
                .commissionAmount(BigDecimal.valueOf(122))
                .deliveryDate(new Date())
                .paymentDate(new Date())
                .itemCreationDate(new Date())
                .transactionType(TransactionType.Sale)
                .build();

        SettlementItem settlement2 = SettlementItem.builder()
                .sellerId(1L)
                .commissionAmount(BigDecimal.valueOf(122))
                .deliveryDate(new Date())
                .paymentDate(new Date())
                .transactionType(TransactionType.Sale)
                .itemCreationDate(new Date())
                .build();
        CommissionInvoice commissionInvoice = CommissionInvoice.builder().endDate(new Date(2L)).build();
        when(commissionInvoiceRepository.findTopBySellerIdOrderByEndDateDesc(1L)).thenReturn(commissionInvoice);
        when(settlementItemRepository.findBySellerIdAndItemCreationDateBetween(eq(1L), any(), any())).thenReturn(List.of(settlement1, settlement2));
        VatModel vatModel = new VatModel(BigDecimal.valueOf(21), BigDecimal.valueOf(242), BigDecimal.valueOf(42), BigDecimal.valueOf(200));
        when(vatCalculatorService.calculateVatModel(any(), any())).thenReturn(vatModel);

        //when
        commissionInvoiceService.create(1L, new Date(), "NL", "EUR");
        //then
        ArgumentCaptor<CommissionInvoice> commissionInvoiceCaptor = ArgumentCaptor.forClass(CommissionInvoice.class);
        verify(commissionInvoiceRepository).save(commissionInvoiceCaptor.capture());
        CommissionInvoice actualCommissionInvoice = commissionInvoiceCaptor.getValue();
        assertThat(actualCommissionInvoice.getStartDate().getTime()).isEqualTo(3L);
    }
}