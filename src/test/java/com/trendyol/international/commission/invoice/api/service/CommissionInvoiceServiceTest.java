package com.trendyol.international.commission.invoice.api.service;

import com.trendyol.international.commission.invoice.api.domain.entity.CommissionInvoice;
import com.trendyol.international.commission.invoice.api.domain.entity.SettlementItem;
import com.trendyol.international.commission.invoice.api.domain.event.CommissionInvoiceCreateEvent;
import com.trendyol.international.commission.invoice.api.domain.event.DocumentCreateEvent;
import com.trendyol.international.commission.invoice.api.feign.client.SellerApiClient;
import com.trendyol.international.commission.invoice.api.kafka.producer.CommissionInvoiceCreateProducer;
import com.trendyol.international.commission.invoice.api.kafka.producer.DocumentCreateProducer;
import com.trendyol.international.commission.invoice.api.model.VatModel;
import com.trendyol.international.commission.invoice.api.model.dto.CommissionInvoiceCreateDto;
import com.trendyol.international.commission.invoice.api.model.enums.InvoiceStatus;
import com.trendyol.international.commission.invoice.api.model.enums.TransactionType;
import com.trendyol.international.commission.invoice.api.model.enums.VatStatusType;
import com.trendyol.international.commission.invoice.api.model.response.Seller.*;
import com.trendyol.international.commission.invoice.api.model.response.SellerIdWithAutomaticInvoiceStartDate;
import com.trendyol.international.commission.invoice.api.model.response.SellerIdsWithAutomaticInvoiceStartDate;
import com.trendyol.international.commission.invoice.api.model.response.SellerResponse;
import com.trendyol.international.commission.invoice.api.repository.CommissionInvoiceRepository;
import com.trendyol.international.commission.invoice.api.repository.SettlementItemRepository;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommissionInvoiceServiceTest {

    @InjectMocks
    private CommissionInvoiceService commissionInvoiceService;

    @Mock
    private CommissionInvoiceSerialNumberGenerateService commissionInvoiceSerialNumberGenerateService;

    @Mock
    private VatCalculatorService vatCalculatorService;

    @Mock
    private SellerApiClient sellerApiClient;

    @Mock
    private CommissionInvoiceCreateProducer commissionInvoiceCreateProducer;

    @Mock
    private DocumentCreateProducer documentCreateProducer;

    @Mock
    private CommissionInvoiceRepository commissionInvoiceRepository;

    @Mock
    private SettlementItemRepository settlementItemRepository;

    @Test
    public void it_should_not_create_commission_invoice() {
        //given
        Date automaticInvoiceStartDate = new Date(1648771200000L);

        SellerIdWithAutomaticInvoiceStartDate sellerIdWithAutomaticInvoiceStartDate1 = SellerIdWithAutomaticInvoiceStartDate.builder()
                .sellerId(1L)
                .automaticInvoiceStartDate(automaticInvoiceStartDate)
                .build();
        SellerIdWithAutomaticInvoiceStartDate sellerIdWithAutomaticInvoiceStartDate2 = SellerIdWithAutomaticInvoiceStartDate.builder()
                .sellerId(2L)
                .automaticInvoiceStartDate(automaticInvoiceStartDate)
                .build();
        SellerIdWithAutomaticInvoiceStartDate sellerIdWithAutomaticInvoiceStartDate3 = SellerIdWithAutomaticInvoiceStartDate.builder()
                .sellerId(3L)
                .automaticInvoiceStartDate(automaticInvoiceStartDate)
                .build();
        SellerIdsWithAutomaticInvoiceStartDate sellerIdsWithAutomaticInvoiceStartDate = SellerIdsWithAutomaticInvoiceStartDate.builder()
                .count(3)
                .sellerIds(List.of(sellerIdWithAutomaticInvoiceStartDate1, sellerIdWithAutomaticInvoiceStartDate2, sellerIdWithAutomaticInvoiceStartDate3))
                .build();
        when(sellerApiClient.getWeeklyInvoiceEnabledSellers()).thenReturn(sellerIdsWithAutomaticInvoiceStartDate);
        //when
        commissionInvoiceService.create();
        //then
        ArgumentCaptor<CommissionInvoiceCreateEvent> commissionInvoiceCreateMessageArgumentCaptor = ArgumentCaptor.forClass(CommissionInvoiceCreateEvent.class);
        verify(commissionInvoiceCreateProducer, times(3)).produceCommissionInvoiceCreateMessage(commissionInvoiceCreateMessageArgumentCaptor.capture());

        List<CommissionInvoiceCreateEvent> commissionInvoiceCreateEventList = commissionInvoiceCreateMessageArgumentCaptor.getAllValues();

        assertThat(commissionInvoiceCreateEventList.stream().map(CommissionInvoiceCreateEvent::getSellerId).collect(Collectors.toList())).containsExactly(1L, 2L, 3L);
    }

    @Test
    public void it_should_not_create_commission_invoice_for_seller_when_there_is_no_settlement() {
        //given
        Date automaticInvoiceStartDate = new Date(1648771200000L);
        Date endDate = new Date(1651363199000L);

        when(settlementItemRepository.getSettlementItems(eq(1L), any(), any())).thenReturn(List.of());
        //when
        commissionInvoiceService.createCommissionInvoiceForSeller(CommissionInvoiceCreateDto.builder()
                .sellerId(1L)
                .country("NL")
                .currency("EUR")
                .automaticInvoiceStartDate(automaticInvoiceStartDate)
                .endDate(endDate)
                .build());
        //then
        verify(commissionInvoiceRepository, never()).save(any());
    }

    @Test
    public void it_should_not_create_commission_invoice_for_seller_when_total_amount_is_negative() {
        //given
        Date automaticInvoiceStartDate = new Date(1648771200000L);
        Date endDate = new Date(1651363199000L);

        SettlementItem settlement1 = SettlementItem.builder()
                .sellerId(1L)
                .commissionAmount(BigDecimal.valueOf(121))
                .deliveryDate(new Date())
                .paymentDate(new Date())
                .itemCreationDate(new Date())
                .transactionType(TransactionType.SALE)
                .build();

        SettlementItem settlement2 = SettlementItem.builder()
                .sellerId(1L)
                .commissionAmount(BigDecimal.valueOf(122))
                .deliveryDate(new Date())
                .paymentDate(new Date())
                .transactionType(TransactionType.RETURN)
                .itemCreationDate(new Date())
                .build();
        when(settlementItemRepository.getSettlementItems(eq(1L), any(), any())).thenReturn(List.of(settlement1, settlement2));

        //when
        commissionInvoiceService.createCommissionInvoiceForSeller(CommissionInvoiceCreateDto.builder()
                .sellerId(1L)
                .country("NL")
                .currency("EUR")
                .automaticInvoiceStartDate(automaticInvoiceStartDate)
                .endDate(endDate)
                .build());
        //then
        verify(commissionInvoiceRepository, never()).save(any());
    }

    @Test
    public void it_should_not_create_commission_invoice_when_total_amount_is_zero() {
        //given
        Date automaticInvoiceStartDate = new Date(1648771200000L);
        Date endDate = new Date(1651363199000L);

        SettlementItem settlement1 = SettlementItem.builder()
                .sellerId(1L)
                .commissionAmount(BigDecimal.valueOf(121))
                .deliveryDate(new Date())
                .paymentDate(new Date())
                .itemCreationDate(new Date())
                .transactionType(TransactionType.SALE)
                .build();

        SettlementItem settlement2 = SettlementItem.builder()
                .sellerId(1L)
                .commissionAmount(BigDecimal.valueOf(121))
                .deliveryDate(new Date())
                .paymentDate(new Date())
                .transactionType(TransactionType.RETURN)
                .itemCreationDate(new Date())
                .build();
        when(settlementItemRepository.getSettlementItems(eq(1L), any(), any())).thenReturn(List.of(settlement1, settlement2));

        //when
        commissionInvoiceService.createCommissionInvoiceForSeller(CommissionInvoiceCreateDto.builder()
                .sellerId(1L)
                .country("NL")
                .currency("EUR")
                .automaticInvoiceStartDate(automaticInvoiceStartDate)
                .endDate(endDate)
                .build());
        //then
        verify(commissionInvoiceRepository, never()).save(any());
    }

    @Test
    public void it_should_create_commission_invoice_for_seller_when_seller_has_no_previous_commission_invoice() {
        //given
        Date automaticInvoiceStartDate = new Date(1648771200000L);
        Date endDate = new Date(1651363199000L);

        SettlementItem settlement1 = SettlementItem.builder()
                .sellerId(1L)
                .commissionAmount(BigDecimal.valueOf(121))
                .deliveryDate(new Date())
                .paymentDate(new Date())
                .itemCreationDate(new Date())
                .transactionType(TransactionType.SALE)
                .build();

        SettlementItem settlement2 = SettlementItem.builder()
                .sellerId(1L)
                .commissionAmount(BigDecimal.valueOf(121))
                .deliveryDate(new Date())
                .paymentDate(new Date())
                .transactionType(TransactionType.SALE)
                .itemCreationDate(new Date())
                .build();

        when(commissionInvoiceRepository.findTopBySellerIdOrderByEndDateDesc(1L)).thenReturn(Optional.empty());
        when(settlementItemRepository.getSettlementItems(eq(1L), any(), any())).thenReturn(List.of(settlement1, settlement2));
        VatModel vatModel = new VatModel(BigDecimal.valueOf(21), BigDecimal.valueOf(242), BigDecimal.valueOf(42), BigDecimal.valueOf(200));
        when(vatCalculatorService.calculateVatModel(BigDecimal.valueOf(242), BigDecimal.valueOf(21))).thenReturn(vatModel);
        //when
        commissionInvoiceService.createCommissionInvoiceForSeller(CommissionInvoiceCreateDto.builder()
                .sellerId(1L)
                .country("NL")
                .currency("EUR")
                .automaticInvoiceStartDate(automaticInvoiceStartDate)
                .endDate(endDate)
                .build());
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
        assertThat(actualCommissionInvoice.getStartDate()).isEqualTo(automaticInvoiceStartDate);
        assertThat(actualCommissionInvoice.getEndDate()).isEqualTo(endDate);
    }

    @Test
    public void it_should_create_commission_invoice_for_seller_when_seller_has_previous_commission_invoice() {
        //given
        Date previousCommissionInvoiceEndDate = new Date(1648771199999L);
        Date automaticInvoiceStartDate = new Date(1648771100000L);
        Date endDate = new Date(1651363199000L);

        SettlementItem settlement1 = SettlementItem.builder()
                .sellerId(1L)
                .commissionAmount(BigDecimal.valueOf(121))
                .deliveryDate(new Date())
                .paymentDate(new Date())
                .itemCreationDate(new Date())
                .transactionType(TransactionType.SALE)
                .build();

        SettlementItem settlement2 = SettlementItem.builder()
                .sellerId(1L)
                .commissionAmount(BigDecimal.valueOf(121))
                .deliveryDate(new Date())
                .paymentDate(new Date())
                .transactionType(TransactionType.SALE)
                .itemCreationDate(new Date())
                .build();
        CommissionInvoice commissionInvoice = CommissionInvoice.builder().endDate(previousCommissionInvoiceEndDate).build();
        when(commissionInvoiceRepository.findTopBySellerIdOrderByEndDateDesc(1L)).thenReturn(Optional.of(commissionInvoice));
        when(settlementItemRepository.getSettlementItems(eq(1L), any(), any())).thenReturn(List.of(settlement1, settlement2));
        VatModel vatModel = new VatModel(BigDecimal.valueOf(21), BigDecimal.valueOf(242), BigDecimal.valueOf(42), BigDecimal.valueOf(200));
        when(vatCalculatorService.calculateVatModel(any(), any())).thenReturn(vatModel);
        //when
        commissionInvoiceService.createCommissionInvoiceForSeller(CommissionInvoiceCreateDto.builder()
                .sellerId(1L)
                .country("NL")
                .currency("EUR")
                .automaticInvoiceStartDate(automaticInvoiceStartDate)
                .endDate(endDate)
                .build());
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
        assertThat(actualCommissionInvoice.getStartDate()).isEqualTo(new Date(1648771200000L));
        assertThat(actualCommissionInvoice.getEndDate()).isEqualTo(endDate);
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
        ArgumentCaptor<DocumentCreateEvent> documentCreateMessageArgumentCaptor = ArgumentCaptor.forClass(DocumentCreateEvent.class);
        verify(documentCreateProducer, times(2)).produceDocumentCreateMessage(documentCreateMessageArgumentCaptor.capture());

        List<DocumentCreateEvent> documentCreateEvents = documentCreateMessageArgumentCaptor.getAllValues();

        Optional<DocumentCreateEvent> documentCreateMessage1 = documentCreateEvents.stream().filter(f -> f.getSellerId().equals(1L)).findFirst();
        assertThat(documentCreateMessage1).isPresent();
        assertThat(documentCreateMessage1.get().getSellerId()).isEqualTo(1L);
        assertThat(documentCreateMessage1.get().getSellerName()).isEqualTo("Mert Unsal");
        assertThat(documentCreateMessage1.get().getAddressLine()).isEqualTo("Lumina Hause, 89 New Bond Street W1S 1DA Amsterdam/Netherland");
        assertThat(documentCreateMessage1.get().getEmail()).isEqualTo("mert.unsal@trendyol.com");
        assertThat(documentCreateMessage1.get().getPhone()).isEqualTo("+905555555555");
        assertThat(documentCreateMessage1.get().getInvoiceNumber()).isEqualTo("TBV2022000000001");
        assertThat(documentCreateMessage1.get().getInvoiceDate()).isEqualTo(currentDate);
        assertThat(documentCreateMessage1.get().getTaxIdentificationNumber()).isEqualTo("1234567890");
        assertThat(documentCreateMessage1.get().getVatRegistrationNumber()).isEqualTo("NL1234567890");
        assertThat(documentCreateMessage1.get().getReferenceId()).isEqualTo("1234567890");
        assertThat(documentCreateMessage1.get().getVatStatusType()).isEqualTo(VatStatusType.DOMESTIC.name());
        assertThat(documentCreateMessage1.get().getNetAmount()).isEqualTo(BigDecimal.valueOf(100L));
        assertThat(documentCreateMessage1.get().getVatAmount()).isEqualTo(BigDecimal.valueOf(21L));
        assertThat(documentCreateMessage1.get().getVatRate()).isEqualTo(BigDecimal.valueOf(21L));
        assertThat(documentCreateMessage1.get().getAmount()).isEqualTo(BigDecimal.valueOf(121L));

        Optional<DocumentCreateEvent> documentCreateMessage2 = documentCreateEvents.stream().filter(f -> f.getSellerId().equals(2L)).findFirst();
        assertThat(documentCreateMessage2).isPresent();
        assertThat(documentCreateMessage2.get().getSellerId()).isEqualTo(2L);
        assertThat(documentCreateMessage2.get().getSellerName()).isEqualTo("Okan Uslu");
        assertThat(documentCreateMessage2.get().getAddressLine()).isEqualTo("Lumina Hause, 90 New Bond Street W1S 1DA Amsterdam/Netherland");
        assertThat(documentCreateMessage2.get().getEmail()).isEqualTo("okan.uslu@trendyol.com");
        assertThat(documentCreateMessage2.get().getPhone()).isEqualTo("+905555555556");
        assertThat(documentCreateMessage2.get().getInvoiceNumber()).isEqualTo("TBV2022000000002");
        assertThat(documentCreateMessage2.get().getInvoiceDate()).isEqualTo(currentDate);
        assertThat(documentCreateMessage2.get().getTaxIdentificationNumber()).isEqualTo("1234567891");
        assertThat(documentCreateMessage2.get().getVatRegistrationNumber()).isEqualTo("NL1234567891");
        assertThat(documentCreateMessage2.get().getReferenceId()).isEqualTo("1234567891");
        assertThat(documentCreateMessage2.get().getVatStatusType()).isEqualTo(VatStatusType.DOMESTIC.name());
        assertThat(documentCreateMessage2.get().getNetAmount()).isEqualTo(BigDecimal.valueOf(200L));
        assertThat(documentCreateMessage2.get().getVatAmount()).isEqualTo(BigDecimal.valueOf(42L));
        assertThat(documentCreateMessage2.get().getVatRate()).isEqualTo(BigDecimal.valueOf(21L));
        assertThat(documentCreateMessage2.get().getAmount()).isEqualTo(BigDecimal.valueOf(242L));
    }
}