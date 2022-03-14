package com.trendyol.international.commission.invoice.api.service;

import com.trendyol.international.commission.invoice.api.domain.CommissionInvoice;
import com.trendyol.international.commission.invoice.api.domain.SettlementItem;
import com.trendyol.international.commission.invoice.api.model.VatModel;
import com.trendyol.international.commission.invoice.api.model.dto.CommissionInvoiceDto;
import com.trendyol.international.commission.invoice.api.model.enums.InvoiceStatus;
import com.trendyol.international.commission.invoice.api.repository.CommissionInvoiceRepository;
import com.trendyol.international.commission.invoice.api.repository.SettlementItemRepository;
import com.trendyol.international.commission.invoice.api.types.VatStatusType;
import com.trendyol.international.commission.invoice.api.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class CommissionInvoiceService {

    private final CommissionInvoiceRepository commissionInvoiceRepository;

    private final SettlementItemRepository settlementItemRepository;

    private final VatCalculatorService vatCalculatorService;

    private final CommissionInvoiceSerialNumberGenerateService commissionInvoiceSerialNumberGenerateService;

    public CommissionInvoice saveCommissionInvoice(CommissionInvoice commissionInvoice) {
        return commissionInvoiceRepository.save(commissionInvoice);
    }

    // Collect all settlement items and processSettlementItems
    public void create(Long sellerId, Date jobExecutionDate, String country, String currency) {
        Date startDate = getStartDateForSeller(sellerId);
        Date endDate = getEndDate(jobExecutionDate);

        List<SettlementItem> settlementItems = settlementItemRepository.findBySellerIdAndItemCreationDateBetween(sellerId, startDate, endDate);
        if (settlementItems.isEmpty()) {
            log.warn("There is no eligible settlement record for creating commission invoice. sellerId: {}", sellerId);
            return;
        }
        CommissionInvoiceDto commissionInvoiceDto = CommissionInvoiceDto.builder().sellerId(sellerId).startDate(startDate).endDate(endDate).settlementItems(settlementItems).build();
        BigDecimal commissionAmount = calculateCommissionForSeller(commissionInvoiceDto);

        if (BigDecimal.ZERO.compareTo(commissionAmount) >= 0) {
            log.warn("Non-positive total commission amount. sellerId: {}", sellerId);
            return;
        }

        VatModel vatModel = vatCalculatorService.calculateVatModel(commissionAmount, "NL".equals(country) ? BigDecimal.valueOf(21) : BigDecimal.ZERO);
        String serialNumber = commissionInvoiceSerialNumberGenerateService.generate(DateUtils.getYear(endDate));

        CommissionInvoice commissionInvoice = CommissionInvoice
                .builder()
                .sellerId(sellerId)
                .amount(commissionAmount)
                .settlementItems(Set.copyOf(settlementItems))
                .country(country)
                .currency(currency)
                .netAmount(vatModel.getNetAmount())
                .vatAmount(vatModel.getVatAmount())
                .vatRate(vatModel.getVatRate())
                .invoiceStatus(InvoiceStatus.CREATED)
                .invoiceDate(endDate)
                .chargedVatDescription("commission invoice")
                .endDate(endDate)
                .startDate(startDate)
                .serialNumber(serialNumber)
                .storeFrontId("1")
                .vatStatusType("NL".equals(country) ? VatStatusType.DOMESTIC : VatStatusType.INTRA_COMMUNITY)
                .build();
        commissionInvoiceRepository.save(commissionInvoice);
    }


    public BigDecimal calculateCommissionForSeller(CommissionInvoiceDto commissionInvoiceDto) {
        return commissionInvoiceDto.getSettlementItems()
                .stream()
                .map(SettlementItem::getCommissionAmountSignedValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Date getStartDateForSeller(Long sellerId) {
        return Optional.ofNullable(commissionInvoiceRepository.findTopBySellerIdOrderByEndDateDesc(sellerId))
                .map(c -> new Date(c.getEndDate().getTime()+1))
                .orElse(new Date());

    }
    public LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public Date getEndDate(Date date) {
        LocalDateTime localDateTime = convertToLocalDateTimeViaInstant(date).minusDays(1);
        return Date.from(LocalDateTime.of(
                localDateTime.getYear(),
                localDateTime.getMonth(),
                localDateTime.getDayOfMonth(),
                23,
                59,
                59,
                999_000_000).atZone(ZoneId.of("Europe/Amsterdam")).toInstant());
    }
}
