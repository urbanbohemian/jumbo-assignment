package com.trendyol.international.commission.invoice.api.service;

import com.trendyol.international.commission.invoice.api.domain.CommissionInvoice;
import com.trendyol.international.commission.invoice.api.domain.SettlementItem;
import com.trendyol.international.commission.invoice.api.model.VatModel;
import com.trendyol.international.commission.invoice.api.model.dto.CommissionInvoiceCreateDto;
import com.trendyol.international.commission.invoice.api.model.enums.InvoiceStatus;
import com.trendyol.international.commission.invoice.api.model.enums.VatStatusType;
import com.trendyol.international.commission.invoice.api.repository.CommissionInvoiceRepository;
import com.trendyol.international.commission.invoice.api.repository.SettlementItemRepository;
import com.trendyol.international.commission.invoice.api.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class CommissionInvoiceService {
    private final CommissionInvoiceSerialNumberGenerateService commissionInvoiceSerialNumberGenerateService;
    private final VatCalculatorService vatCalculatorService;
    private final CommissionInvoiceRepository commissionInvoiceRepository;
    private final SettlementItemRepository settlementItemRepository;

    // collects all settlement items and process
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
                .build();
        commissionInvoiceRepository.save(commissionInvoice);
    }

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
}
