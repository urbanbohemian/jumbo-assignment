package com.trendyol.international.commission.invoice.api.service;

import com.trendyol.international.commission.invoice.api.client.SellerApiClient;
import com.trendyol.international.commission.invoice.api.domain.CommissionInvoice;
import com.trendyol.international.commission.invoice.api.domain.event.InvoiceLine;
import com.trendyol.international.commission.invoice.api.domain.SettlementItem;
import com.trendyol.international.commission.invoice.api.domain.event.DocumentCreateMessage;
import com.trendyol.international.commission.invoice.api.model.VatModel;
import com.trendyol.international.commission.invoice.api.model.dto.CommissionInvoiceCreateDto;
import com.trendyol.international.commission.invoice.api.model.enums.InvoiceStatus;
import com.trendyol.international.commission.invoice.api.model.enums.VatStatusType;
import com.trendyol.international.commission.invoice.api.model.response.Seller.Address;
import com.trendyol.international.commission.invoice.api.model.response.SellerResponse;
import com.trendyol.international.commission.invoice.api.producer.DocumentCreateProducer;
import com.trendyol.international.commission.invoice.api.repository.CommissionInvoiceRepository;
import com.trendyol.international.commission.invoice.api.repository.SettlementItemRepository;
import com.trendyol.international.commission.invoice.api.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class CommissionInvoiceService {
    private final CommissionInvoiceSerialNumberGenerateService commissionInvoiceSerialNumberGenerateService;
    private final VatCalculatorService vatCalculatorService;
    private final SellerApiClient sellerApiClient;
    private final DocumentCreateProducer documentCreateProducer;
    private final CommissionInvoiceRepository commissionInvoiceRepository;
    private final SettlementItemRepository settlementItemRepository;

    private Date getStartDateForSeller(Long sellerId) {
        return Optional.ofNullable(commissionInvoiceRepository.findTopBySellerIdOrderByEndDateDesc(sellerId))
                .map(commissionInvoice -> new Date(commissionInvoice.getEndDate().getTime() + 1))
                .orElse(new Date());
    }

    private Date getEndDate(Date date) {
        LocalDateTime localDateTime = DateUtils.convertToLocalDateTime(date).minusDays(1);
        return Date.from(LocalDateTime.of(
                localDateTime.getYear(),
                localDateTime.getMonth(),
                localDateTime.getDayOfMonth(),
                23,
                59,
                59,
                999_000_000).atZone(ZoneId.of("Europe/Amsterdam")).toInstant());
    }

    private BigDecimal calculateCommissionForSeller(List<SettlementItem> settlementItems) {
        return settlementItems
                .stream()
                .map(SettlementItem::getCommissionAmountSignedValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // collects all settlement items and process
    @Transactional
    public void create(CommissionInvoiceCreateDto commissionInvoiceCreateDto) {
        Date startDate = getStartDateForSeller(commissionInvoiceCreateDto.getSellerId());
        Date endDate = getEndDate(commissionInvoiceCreateDto.getJobExecutionDate());

        List<SettlementItem> settlementItems = settlementItemRepository.findBySellerIdAndItemCreationDateBetween(commissionInvoiceCreateDto.getSellerId(), startDate, endDate);
        if (settlementItems.isEmpty()) {
            log.warn("There is no eligible settlement record for creating commission invoice. sellerId: {}", commissionInvoiceCreateDto.getSellerId());
            return;
        }

        BigDecimal commissionAmount = calculateCommissionForSeller(settlementItems);
        if (BigDecimal.ZERO.compareTo(commissionAmount) >= 0) {
            log.warn("Non-positive total commission amount. sellerId: {}", commissionInvoiceCreateDto.getSellerId());
            return;
        }

        VatModel vatModel = vatCalculatorService.calculateVatModel(commissionAmount, "NL".equals(commissionInvoiceCreateDto.getCountry()) ? BigDecimal.valueOf(21) : BigDecimal.ZERO);

        CommissionInvoice commissionInvoice = CommissionInvoice
                .builder()
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
                .chargedVatDescription("commission invoice")
                .endDate(endDate)
                .startDate(startDate)
                .storeFrontId("1")
                .vatStatusType("NL".equals(commissionInvoiceCreateDto.getCountry()) ? VatStatusType.DOMESTIC : VatStatusType.INTRA_COMMUNITY)
                .referenceId(UUID.randomUUID().toString())
                .build();
        commissionInvoiceRepository.save(commissionInvoice);
    }

    private void generateSerialNumberForCommissionInvoice(CommissionInvoice commissionInvoice) {
        Integer invoiceYear = DateUtils.getYear(commissionInvoice.getEndDate());
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

    private DocumentCreateMessage getDocumentCreateMessage(Long sellerId, SellerResponse sellerResponse, List<CommissionInvoice> commissionInvoices) {
        return DocumentCreateMessage.builder()
                .sellerId(sellerId)
                .sellerName(sellerResponse.getCompanyName())
                .addressLine(sellerResponse.getInvoiceAddress().map(Address::getFormattedAddress).orElse(StringUtils.EMPTY))
                .email(sellerResponse.getMasterUser().getContact().getEmail())
                .phone(sellerResponse.getMasterUser().getContact().getPhone().getFullPhoneNumber())
                .taxIdentificationNumber(sellerResponse.getTaxNumber())
                .vatRegistrationNumber(sellerResponse.getVatRegistrationNumber())
                .invoiceLineList(commissionInvoices.stream().map(commissionInvoice -> InvoiceLine.builder()
                        .description("Commission Fee")
                        .quantity(1)
                        .unit("Item")
                        .unitPrice(commissionInvoice.getNetAmount())
                        .vatRate(commissionInvoice.getVatRate())
                        .amount(commissionInvoice.getAmount())
                        .referenceId(commissionInvoice.getReferenceId())
                        .invoiceNumber(commissionInvoice.getSerialNumber())
                        .invoiceDate(commissionInvoice.getInvoiceDate())
                        .vatStatusType(commissionInvoice.getVatStatusType().toString())
                        .build()).toList())
                .build();
    }

    private void generatePdfForSeller(Long sellerId, List<CommissionInvoice> commissionInvoices) {
        SellerResponse sellerResponse = sellerApiClient.getSellerById(sellerId);
        DocumentCreateMessage documentCreateMessage = getDocumentCreateMessage(sellerId, sellerResponse, commissionInvoices);
        documentCreateProducer.produceDocumentCreateMessage(documentCreateMessage);
    }

    @Transactional
    public void generatePdf() {
        commissionInvoiceRepository
                .findByInvoiceStatus(InvoiceStatus.NUMBER_GENERATED)
                .stream()
                .collect(Collectors.groupingBy(CommissionInvoice::getSellerId))
                .forEach(this::generatePdfForSeller);
    }
}
