package com.trendyol.international.commission.invoice.api.service;

import com.trendyol.international.commission.invoice.api.client.SellerApiClient;
import com.trendyol.international.commission.invoice.api.domain.CommissionInvoice;
import com.trendyol.international.commission.invoice.api.domain.InvoiceLine;
import com.trendyol.international.commission.invoice.api.domain.SettlementItem;
import com.trendyol.international.commission.invoice.api.domain.event.DocumentCreateMessage;
import com.trendyol.international.commission.invoice.api.model.VatModel;
import com.trendyol.international.commission.invoice.api.model.dto.CommissionInvoiceCreateDto;
import com.trendyol.international.commission.invoice.api.model.enums.InvoiceStatus;
import com.trendyol.international.commission.invoice.api.model.enums.TransactionType;
import com.trendyol.international.commission.invoice.api.model.enums.VatStatusType;
import com.trendyol.international.commission.invoice.api.model.response.Seller.*;
import com.trendyol.international.commission.invoice.api.model.response.SellerResponse;
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
import java.util.Optional;

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

    @Test
    public void it_should_generate_pdf() {
        //given
        Date currentDate = new Date();

        CommissionInvoice commissionInvoice1 = CommissionInvoice.builder()
                .sellerId(1L)
                .serialNumber("TBV2022000000001")
                .amount(BigDecimal.valueOf(121L))
                .netAmount(BigDecimal.valueOf(100L))
                .vatAmount(BigDecimal.valueOf(21L))
                .vatRate(BigDecimal.valueOf(21L))
                .vatStatusType(VatStatusType.DOMESTIC)
                .invoiceDate(currentDate)
                .storeFrontId("1")
                .country("Netherlands")
                .currency("EU")
                .startDate(currentDate)
                .endDate(currentDate)
                .invoiceStatus(InvoiceStatus.NUMBER_GENERATED)
                .referenceId("1234567890")
                .build();
        CommissionInvoice commissionInvoice2 = CommissionInvoice.builder()
                .sellerId(2L)
                .serialNumber("TBV2022000000002")
                .amount(BigDecimal.valueOf(242L))
                .netAmount(BigDecimal.valueOf(200L))
                .vatAmount(BigDecimal.valueOf(42L))
                .vatRate(BigDecimal.valueOf(21L))
                .vatStatusType(VatStatusType.DOMESTIC)
                .invoiceDate(currentDate)
                .storeFrontId("1")
                .country("Netherlands")
                .currency("EU")
                .startDate(currentDate)
                .endDate(currentDate)
                .invoiceStatus(InvoiceStatus.NUMBER_GENERATED)
                .referenceId("1234567891")
                .build();

        SellerResponse sellerResponse1 = SellerResponse.builder()
                .companyName("Mert Unsal")
                .addresses(List.of(Address.builder()
                        .addressLine("Lumina Hause, 89 New Bond Street W1S 1DA")
                        .addressType(AddressType.INVOICE_ADDRESS)
                        .country("Netherland")
                        .district("Amsterdam")
                        .build()))
                .masterUser(MasterUser.builder().contact(Contact.builder()
                        .email("mert.unsal@trendyol.com")
                        .phone(Phone.builder().countryCode("+90").phone("5555555555").build())
                        .build()).build())
                .taxNumber("1234567890")
                .countryBasedIn("NL")
                .build();
        SellerResponse sellerResponse2 = SellerResponse.builder()
                .companyName("Okan Uslu")
                .addresses(List.of(Address.builder()
                        .addressLine("Lumina Hause, 90 New Bond Street W1S 1DA")
                        .addressType(AddressType.INVOICE_ADDRESS)
                        .country("Netherland")
                        .district("Amsterdam")
                        .build()))
                .masterUser(MasterUser.builder().contact(Contact.builder()
                        .email("okan.uslu@trendyol.com")
                        .phone(Phone.builder().countryCode("+90").phone("5555555556").build())
                        .build()).build())
                .taxNumber("1234567891")
                .countryBasedIn("NL")
                .build();

        when(commissionInvoiceRepository.findByInvoiceStatus(InvoiceStatus.NUMBER_GENERATED)).thenReturn(List.of(commissionInvoice1, commissionInvoice2));
        when(sellerApiClient.getSellerById(1L)).thenReturn(sellerResponse1);
        when(sellerApiClient.getSellerById(2L)).thenReturn(sellerResponse2);

        //then
        commissionInvoiceService.generatePdf();

        //then
        ArgumentCaptor<DocumentCreateMessage> documentCreateMessageArgumentCaptor = ArgumentCaptor.forClass(DocumentCreateMessage.class);
        verify(documentCreateProducer, times(2)).produceDocumentCreateMessage(documentCreateMessageArgumentCaptor.capture());

        List<DocumentCreateMessage> documentCreateMessages = documentCreateMessageArgumentCaptor.getAllValues();

        Optional<DocumentCreateMessage> documentCreateMessage1 = documentCreateMessages.stream().filter(f -> f.getSellerId().equals(1L)).findFirst();
        assertThat(documentCreateMessage1).isPresent();
        assertThat(documentCreateMessage1.get().getSellerId()).isEqualTo(1L);
        assertThat(documentCreateMessage1.get().getSellerName()).isEqualTo("Mert Unsal");
        assertThat(documentCreateMessage1.get().getAddressLine()).isEqualTo("Lumina Hause, 89 New Bond Street W1S 1DA Amsterdam/Netherland");
        assertThat(documentCreateMessage1.get().getEmail()).isEqualTo("mert.unsal@trendyol.com");
        assertThat(documentCreateMessage1.get().getPhone()).isEqualTo("+905555555555");
        assertThat(documentCreateMessage1.get().getTaxIdentificationNumber()).isEqualTo("1234567890");
        assertThat(documentCreateMessage1.get().getVatRegistrationNumber()).isEqualTo("NL1234567890");
        assertThat(documentCreateMessage1.get().getInvoiceLineList()).isEqualTo(List.of(InvoiceLine.builder()
                .description("Commission Fee")
                .quantity(1)
                .unit("Item")
                .unitPrice(BigDecimal.valueOf(100L))
                .vatRate(BigDecimal.valueOf(21L))
                .amount(BigDecimal.valueOf(121L))
                .referenceId("1234567890")
                .invoiceNumber("TBV2022000000001")
                .invoiceDate(currentDate)
                .vatStatusType(VatStatusType.DOMESTIC.name())
                .build()));

        Optional<DocumentCreateMessage> documentCreateMessage2 = documentCreateMessages.stream().filter(f -> f.getSellerId().equals(2L)).findFirst();
        assertThat(documentCreateMessage2).isPresent();
        assertThat(documentCreateMessage2.get().getSellerId()).isEqualTo(2L);
        assertThat(documentCreateMessage2.get().getSellerName()).isEqualTo("Okan Uslu");
        assertThat(documentCreateMessage2.get().getAddressLine()).isEqualTo("Lumina Hause, 90 New Bond Street W1S 1DA Amsterdam/Netherland");
        assertThat(documentCreateMessage2.get().getEmail()).isEqualTo("okan.uslu@trendyol.com");
        assertThat(documentCreateMessage2.get().getPhone()).isEqualTo("+905555555556");
        assertThat(documentCreateMessage2.get().getTaxIdentificationNumber()).isEqualTo("1234567891");
        assertThat(documentCreateMessage2.get().getVatRegistrationNumber()).isEqualTo("NL1234567891");
        assertThat(documentCreateMessage2.get().getInvoiceLineList()).isEqualTo(List.of(InvoiceLine.builder()
                .description("Commission Fee")
                .quantity(1)
                .unit("Item")
                .unitPrice(BigDecimal.valueOf(200L))
                .vatRate(BigDecimal.valueOf(21L))
                .amount(BigDecimal.valueOf(242L))
                .referenceId("1234567891")
                .invoiceNumber("TBV2022000000002")
                .invoiceDate(currentDate)
                .vatStatusType(VatStatusType.DOMESTIC.name())
                .build()));
    }
}