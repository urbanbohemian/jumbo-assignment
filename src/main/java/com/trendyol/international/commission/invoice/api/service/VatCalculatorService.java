package com.trendyol.international.commission.invoice.api.service;

import com.trendyol.international.commission.invoice.api.model.VatModel;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class VatCalculatorService {

    private static final BigDecimal ONE_HUNDRED_BD = new BigDecimal(100);
    private static final int SCALE = 4;

    public VatModel calculateVatModel(BigDecimal amount, BigDecimal vatRate) {
        final BigDecimal withVatAmountMultiplier = divide(ONE_HUNDRED_BD.add(vatRate), ONE_HUNDRED_BD);
        amount = amount.setScale(2, RoundingMode.HALF_UP);

        BigDecimal netAmount = divide(amount, withVatAmountMultiplier);
        netAmount = netAmount.setScale(2, RoundingMode.HALF_UP);
        BigDecimal vatAmount = amount.subtract(netAmount);

        return new VatModel(vatRate, amount, vatAmount, netAmount);
    }

    private static BigDecimal divide(BigDecimal value, BigDecimal divisor) {
        return value.divide(divisor, SCALE, RoundingMode.UP);
    }
}
