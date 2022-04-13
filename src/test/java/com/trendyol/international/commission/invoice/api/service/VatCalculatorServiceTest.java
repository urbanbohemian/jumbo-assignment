package com.trendyol.international.commission.invoice.api.service;

import com.trendyol.international.commission.invoice.api.model.VatModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class VatCalculatorServiceTest {

    @InjectMocks
    private VatCalculatorService vatCalculatorService;

    @Test
    public void it_should_return_vat_model() {
        //given
        BigDecimal amount = BigDecimal.valueOf(0.048999979);

        //when
        VatModel vatModel = vatCalculatorService.calculateVatModel(amount, BigDecimal.valueOf(8));

        //then
        assertThat(vatModel).isNotNull();
    }

    @Test
    public void it_should_calculate_net_and_vat_amount() {
        //given
        BigDecimal amount = BigDecimal.valueOf(150D);

        //when
        VatModel vatModel = vatCalculatorService.calculateVatModel(amount, BigDecimal.valueOf(18));

        //then
        assertThat(vatModel.getVatAmount().compareTo(BigDecimal.valueOf(22.88D))).isEqualTo(0);
        assertThat(vatModel.getAmount().compareTo(BigDecimal.valueOf(150D))).isEqualTo(0);
        assertThat(vatModel.getNetAmount().compareTo(BigDecimal.valueOf(127.12D))).isEqualTo(0);
        assertThat(vatModel.getVatRate().compareTo(BigDecimal.valueOf(18))).isEqualTo(0);
    }

    @Test
    public void it_should_calculate_net_and_vat_amount_with_another_amount() {
        //given
        BigDecimal amount = BigDecimal.valueOf(100D);

        //when
        VatModel vatModel = vatCalculatorService.calculateVatModel(amount, BigDecimal.valueOf(18));

        //then
        assertThat(vatModel.getVatAmount().compareTo(BigDecimal.valueOf(15.25D))).isEqualTo(0);
        assertThat(vatModel.getAmount().compareTo(BigDecimal.valueOf(100D))).isEqualTo(0);
        assertThat(vatModel.getNetAmount().compareTo(BigDecimal.valueOf(84.75D))).isEqualTo(0);
    }

    @Test
    public void it_should_calculate_net_and_vat_amount_when_vat_rate_zero() {
        //given
        BigDecimal amount = BigDecimal.valueOf(100D);

        //when
        VatModel vatModel = vatCalculatorService.calculateVatModel(amount, BigDecimal.ZERO);

        //then
        assertThat(vatModel.getVatAmount().compareTo(BigDecimal.valueOf(0D))).isEqualTo(0);
        assertThat(vatModel.getAmount().compareTo(BigDecimal.valueOf(100D))).isEqualTo(0);
        assertThat(vatModel.getNetAmount().compareTo(BigDecimal.valueOf(100D))).isEqualTo(0);
    }

    @Test
    public void it_should_calculate_net_and_vat_amount_when_amount_is_zero() {
        //given
        BigDecimal amount = BigDecimal.valueOf(0D);

        //when
        VatModel vatModel = vatCalculatorService.calculateVatModel(amount, BigDecimal.valueOf(18));

        //then
        assertThat(vatModel.getVatAmount().compareTo(BigDecimal.valueOf(0D))).isEqualTo(0);
        assertThat(vatModel.getAmount().compareTo(BigDecimal.valueOf(0D))).isEqualTo(0);
        assertThat(vatModel.getNetAmount().compareTo(BigDecimal.valueOf(0D))).isEqualTo(0);
    }

    @Test
    public void it_should_calculate_net_and_vat_amount_for_rounding_edge_case() {
        //given
        BigDecimal amount = BigDecimal.valueOf(37.99);

        //when
        VatModel vatModel = vatCalculatorService.calculateVatModel(amount, BigDecimal.valueOf(18));

        //then
        assertThat(vatModel.getVatAmount().compareTo(BigDecimal.valueOf(5.80))).isEqualTo(0);
        assertThat(vatModel.getAmount().compareTo(BigDecimal.valueOf(37.99))).isEqualTo(0);
        assertThat(vatModel.getNetAmount().compareTo(BigDecimal.valueOf(32.19))).isEqualTo(0);
    }
}