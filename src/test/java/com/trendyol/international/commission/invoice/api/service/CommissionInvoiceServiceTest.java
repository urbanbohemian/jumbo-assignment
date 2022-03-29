package com.trendyol.international.commission.invoice.api.service;

import com.trendyol.international.commission.invoice.api.client.SellerApiClient;
import com.trendyol.international.commission.invoice.api.domain.CommissionInvoice;
import com.trendyol.international.commission.invoice.api.domain.SettlementItem;
import com.trendyol.international.commission.invoice.api.model.VatModel;
import com.trendyol.international.commission.invoice.api.model.dto.CommissionInvoiceCreateDto;
import com.trendyol.international.commission.invoice.api.model.enums.InvoiceStatus;
import com.trendyol.international.commission.invoice.api.model.enums.TransactionType;
import com.trendyol.international.commission.invoice.api.model.enums.VatStatusType;
import com.trendyol.international.commission.invoice.api.producer.DocumentCreateProducer;
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

    @Mock
    private CommissionInvoiceSerialNumberGenerateService commissionInvoiceSerialNumberGenerateService;

    @Mock
    private SellerApiClient sellerApiClient;

    @Mock
    private DocumentCreateProducer documentCreateProducer;

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
        commissionInvoiceService.create(CommissionInvoiceCreateDto.builder().sellerId(1L).jobExecutionDate(new Date()).country("NL").currency("EUR").build());
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
        commissionInvoiceService.create(CommissionInvoiceCreateDto.builder().sellerId(1L).jobExecutionDate(new Date()).country("NL").currency("EUR").build());
        //then
        verify(commissionInvoiceRepository, never()).save(any());
        verifyNoInteractions(vatCalculatorService);
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
        commissionInvoiceService.create(CommissionInvoiceCreateDto.builder().sellerId(1L).jobExecutionDate(new Date()).country("NL").currency("EUR").build());
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
        commissionInvoiceService.create(CommissionInvoiceCreateDto.builder().sellerId(1L).jobExecutionDate(new Date()).country("NL").currency("EUR").build());
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
        commissionInvoiceService.create(CommissionInvoiceCreateDto.builder().sellerId(1L).jobExecutionDate(new Date()).country("NL").currency("EUR").build());
        //then
        ArgumentCaptor<CommissionInvoice> commissionInvoiceCaptor = ArgumentCaptor.forClass(CommissionInvoice.class);
        verify(commissionInvoiceRepository).save(commissionInvoiceCaptor.capture());
        CommissionInvoice actualCommissionInvoice = commissionInvoiceCaptor.getValue();
        assertThat(actualCommissionInvoice.getStartDate().getTime()).isEqualTo(3L);
    }

    @Test
    public void it_should_generate_serial_number_for_commission_invoices() {
        //given
        CommissionInvoice commissionInvoice1 = CommissionInvoice.builder()
                .sellerId(1L)
                .amount(BigDecimal.valueOf(121L))
                .netAmount(BigDecimal.valueOf(100L))
                .vatAmount(BigDecimal.valueOf(21L))
                .vatRate(BigDecimal.valueOf(21L))
                .vatStatusType(VatStatusType.DOMESTIC)
                .invoiceDate(new Date())
                .storeFrontId("1")
                .country("Netherlands")
                .currency("EU")
                .startDate(new Date())
                .endDate(new Date())
                .invoiceStatus(InvoiceStatus.CREATED)
                .build();
        CommissionInvoice commissionInvoice2 = CommissionInvoice.builder()
                .sellerId(1L)
                .amount(BigDecimal.valueOf(242L))
                .netAmount(BigDecimal.valueOf(200L))
                .vatAmount(BigDecimal.valueOf(42L))
                .vatRate(BigDecimal.valueOf(21L))
                .vatStatusType(VatStatusType.DOMESTIC)
                .invoiceDate(new Date())
                .storeFrontId("1")
                .country("Netherlands")
                .currency("EU")
                .startDate(new Date())
                .endDate(new Date())
                .invoiceStatus(InvoiceStatus.CREATED)
                .build();

        when(commissionInvoiceRepository.findByInvoiceStatus(InvoiceStatus.CREATED)).thenReturn(List.of(commissionInvoice1, commissionInvoice2));
        when(commissionInvoiceSerialNumberGenerateService.generate(anyInt())).thenReturn("TBV2022000000001", "TBV2022000000002");

        //when
        commissionInvoiceService.generateSerialNumber();

        //
        ArgumentCaptor<CommissionInvoice> commissionInvoiceArgumentCaptor = ArgumentCaptor.forClass(CommissionInvoice.class);
        verify(commissionInvoiceRepository, times(2)).save(commissionInvoiceArgumentCaptor.capture());

        List<CommissionInvoice> commissionInvoices = commissionInvoiceArgumentCaptor.getAllValues();
        assertThat(commissionInvoices.stream().map(CommissionInvoice::getSerialNumber)).containsExactly("TBV2022000000001", "TBV2022000000002");
        assertThat(commissionInvoices.stream().allMatch(f -> InvoiceStatus.NUMBER_GENERATED.equals(f.getInvoiceStatus()))).isTrue();
    }
}