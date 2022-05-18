package com.trendyol.international.commission.invoice.api.service;

import com.trendyol.international.commission.invoice.api.domain.entity.CommissionInvoice;
import com.trendyol.international.commission.invoice.api.domain.entity.ErpRequest;
import com.trendyol.international.commission.invoice.api.domain.entity.SettlementItem;
import com.trendyol.international.commission.invoice.api.domain.event.CommissionInvoiceCreateEvent;
import com.trendyol.international.commission.invoice.api.domain.event.DocumentCreateEvent;
import com.trendyol.international.commission.invoice.api.feign.client.SellerApiClient;
import com.trendyol.international.commission.invoice.api.kafka.producer.CommissionInvoiceCreateProducer;
import com.trendyol.international.commission.invoice.api.kafka.producer.DocumentCreateProducer;
import com.trendyol.international.commission.invoice.api.model.VatModel;
import com.trendyol.international.commission.invoice.api.model.dto.CommissionInvoiceCreateDto;
import com.trendyol.international.commission.invoice.api.model.enums.InvoiceStatus;
import com.trendyol.international.commission.invoice.api.model.enums.VatStatusType;
import com.trendyol.international.commission.invoice.api.model.response.Seller.Address;
import com.trendyol.international.commission.invoice.api.model.response.SellerIdWithAutomaticInvoiceStartDate;
import com.trendyol.international.commission.invoice.api.model.response.SellerResponse;
import com.trendyol.international.commission.invoice.api.repository.CommissionInvoiceRepository;
import com.trendyol.international.commission.invoice.api.repository.ErpRequestRepository;
import com.trendyol.international.commission.invoice.api.repository.SettlementItemRepository;
import com.trendyol.international.commission.invoice.api.util.DateUtils;
import com.trendyol.international.commission.invoice.api.util.mapper.ErpRequestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class CommissionInvoiceService {
    private static final String ZONE_ID = "Europe/Amsterdam";
    private static final String COUNTRY = "NL";
    private static final String CURRENCY = "EUR";
    private static final String STORE_FRONT_ID = "1";
    private static final String DESCRIPTION_FORMAT = "Trendyol Commission Fee between %s - %s";

    private final CommissionInvoiceSerialNumberGenerateService commissionInvoiceSerialNumberGenerateService;
    private final VatCalculatorService vatCalculatorService;
    private final SellerApiClient sellerApiClient;
    private final CommissionInvoiceCreateProducer commissionInvoiceCreateProducer;
    private final DocumentCreateProducer documentCreateProducer;
    private final CommissionInvoiceRepository commissionInvoiceRepository;
    private final SettlementItemRepository settlementItemRepository;
    private final ErpRequestRepository erpRequestRepository;
    private final ErpRequestMapper erpRequestMapper;

    private void produceCommissionInvoiceCreateMessageForSeller(SellerIdWithAutomaticInvoiceStartDate sellerIdWithAutomaticInvoiceStartDate) {
        commissionInvoiceCreateProducer.produceCommissionInvoiceCreateMessage(CommissionInvoiceCreateEvent.builder()
                .sellerId(sellerIdWithAutomaticInvoiceStartDate.getSellerId())
                .country(sellerIdWithAutomaticInvoiceStartDate.getCountryBasedIn())
                .currency(sellerIdWithAutomaticInvoiceStartDate.getCurrency())
                .automaticInvoiceStartDate(sellerIdWithAutomaticInvoiceStartDate.getAutomaticInvoiceStartDate())
                .endDate(DateUtils.getLastDateOfMonth(DateUtils.getLocalDateTime(new Date()).minusDays(1), ZONE_ID))
                .build());
    }

    public void create() {
        // TODO: response model should be pageable!
        sellerApiClient.getWeeklyInvoiceEnabledSellers().getSellerIds().forEach(this::produceCommissionInvoiceCreateMessageForSeller);
    }

    private Date getStartDateForSeller(Long sellerId, Date automaticInvoiceStartDate) {
        return commissionInvoiceRepository
                .findTopBySellerIdOrderByEndDateDesc(sellerId)
                .map(commissionInvoice -> new Date(commissionInvoice.getEndDate().getTime() + 1))
                .orElse(automaticInvoiceStartDate);
    }

    private BigDecimal calculateCommissionForSeller(List<SettlementItem> settlementItems) {
        return settlementItems.stream().map(SettlementItem::getCommissionAmountSignedValue).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // collects all settlement items and process
    @Transactional(propagation = Propagation.REQUIRED)
    public void createCommissionInvoiceForSeller(CommissionInvoiceCreateDto commissionInvoiceCreateDto) {
        Date startDate = getStartDateForSeller(commissionInvoiceCreateDto.getSellerId(), commissionInvoiceCreateDto.getAutomaticInvoiceStartDate());
        Date endDate = commissionInvoiceCreateDto.getEndDate();

        List<SettlementItem> settlementItems = settlementItemRepository.getSettlementItems(commissionInvoiceCreateDto.getSellerId(), startDate, endDate);
        if (settlementItems.isEmpty()) {
            log.warn("There is no eligible settlement record for creating commission invoice. sellerId: {}", commissionInvoiceCreateDto.getSellerId());
            return;
        }

        BigDecimal commissionAmount = calculateCommissionForSeller(settlementItems);
        if (BigDecimal.ZERO.compareTo(commissionAmount) >= 0) {
            log.warn("Non-positive total commission amount. sellerId: {}", commissionInvoiceCreateDto.getSellerId());
            return;
        }

        VatModel vatModel = vatCalculatorService.calculateVatModel(commissionAmount, COUNTRY.equals(commissionInvoiceCreateDto.getCountry()) ? BigDecimal.valueOf(21) : BigDecimal.ZERO);

        commissionInvoiceRepository.save(CommissionInvoice.builder()
                .sellerId(commissionInvoiceCreateDto.getSellerId())
                .amount(commissionAmount)
                .settlementItems(Set.copyOf(settlementItems))
                .country(commissionInvoiceCreateDto.getCountry())
                .currency(commissionInvoiceCreateDto.getCurrency())
                .netAmount(vatModel.getNetAmount())
                .vatAmount(vatModel.getVatAmount())
                .vatRate(vatModel.getVatRate())
                .invoiceStatus(InvoiceStatus.CREATED)
                .invoiceDate(endDate)
                .description(String.format(DESCRIPTION_FORMAT, DateUtils.getDateAsStringWithoutYear(startDate), DateUtils.getDateAsStringWithoutYear(endDate)))
                .endDate(endDate)
                .startDate(startDate)
                .storeFrontId(STORE_FRONT_ID)
                .vatStatusType(COUNTRY.equals(commissionInvoiceCreateDto.getCountry()) ? VatStatusType.DOMESTIC : VatStatusType.INTRA_COMMUNITY)
                .referenceId(UUID.randomUUID().toString())
                .build());
    }

    private void generateSerialNumberForCommissionInvoice(CommissionInvoice commissionInvoice) {
        Integer invoiceYear = DateUtils.getYearOfDate(commissionInvoice.getEndDate());
        commissionInvoice.setSerialNumber(commissionInvoiceSerialNumberGenerateService.generate(invoiceYear));
        commissionInvoice.setInvoiceStatus(InvoiceStatus.NUMBER_GENERATED);
        commissionInvoiceRepository.save(commissionInvoice);
    }

    @Transactional
    public void generateSerialNumber() {
        commissionInvoiceRepository
                .findByInvoiceStatus(InvoiceStatus.CREATED)
                .forEach(this::generateSerialNumberForCommissionInvoice);
    }

    private DocumentCreateEvent getDocumentCreateMessage(SellerResponse sellerResponse, CommissionInvoice commissionInvoice) {
        return DocumentCreateEvent.builder()
                .sellerId(commissionInvoice.getSellerId())
                .sellerName(sellerResponse.getCompanyName())
                .addressLine(sellerResponse.getInvoiceAddress().map(Address::getFormattedAddress).orElse(StringUtils.EMPTY))
                .email(sellerResponse.getMasterUser().getContact().getEmail())
                .phone(sellerResponse.getMasterUser().getContact().getPhone().getFullPhoneNumber())
                .invoiceNumber(commissionInvoice.getSerialNumber())
                .invoiceDate(commissionInvoice.getInvoiceDate())
                .taxIdentificationNumber(sellerResponse.getTaxNumber())
                .vatRegistrationNumber(sellerResponse.getVatRegistrationNumber())
                .referenceId(commissionInvoice.getReferenceId())
                .vatStatusType(commissionInvoice.getVatStatusType().name())
                .netAmount(commissionInvoice.getNetAmount())
                .vatAmount(commissionInvoice.getVatAmount())
                .vatRate(commissionInvoice.getVatRate())
                .amount(commissionInvoice.getAmount())
                .build();
    }

    private void generatePdfForSeller(Long sellerId, List<CommissionInvoice> commissionInvoices) {
        var ref = new Object() { SellerResponse sellerResponse = null; };
        try {
            ref.sellerResponse = sellerApiClient.getSellerById(sellerId);
        } catch (Exception exception) {
            log.warn("An error occurred while fetching seller information {}",exception.getMessage());
        }
        if (Objects.nonNull(ref.sellerResponse)) {
            commissionInvoices.forEach(commissionInvoice -> {
                DocumentCreateEvent documentCreateEvent = getDocumentCreateMessage(ref.sellerResponse, commissionInvoice);
                documentCreateProducer.produceDocumentCreateMessage(documentCreateEvent);
            });
        }
    }

    @Transactional
    public void generatePdf() {
        commissionInvoiceRepository
                .findByInvoiceStatus(InvoiceStatus.NUMBER_GENERATED)
                .stream()
                .collect(Collectors.groupingBy(CommissionInvoice::getSellerId))
                .forEach(this::generatePdfForSeller);
    }

    private void envelope(Long sellerId, List<CommissionInvoice> deductionInvoiceList) {
        deductionInvoiceList.forEach(deductionInvoice -> {
            deductionInvoice.setInvoiceStatus(InvoiceStatus.ENVELOPED);
            commissionInvoiceRepository.save(deductionInvoice);
            ErpRequest erpRequest = erpRequestMapper.mapEntityToErpRequest(deductionInvoice);
            erpRequestRepository.save(erpRequest);
        });
    }

    @Transactional
    public void envelope() {
        commissionInvoiceRepository
                .findByInvoiceStatus(InvoiceStatus.PDF_GENERATED)
                .stream()
                .collect(Collectors.groupingBy(CommissionInvoice::getSellerId))
                .forEach(this::envelope);
    }
}
